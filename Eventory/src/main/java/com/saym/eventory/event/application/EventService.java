package com.saym.eventory.event.application;


import com.saym.eventory.bookmark.domain.Bookmark;
import com.saym.eventory.bookmark.domain.repository.BookmarkRepository;
import com.saym.eventory.common.exception.CustomException;
import com.saym.eventory.common.exception.Error;
import com.saym.eventory.event.api.dto.response.EventDetailResponseDto;
import com.saym.eventory.event.api.dto.response.EventInfoResponseDto;
import com.saym.eventory.event.domain.Area;
import com.saym.eventory.event.domain.Event;
import com.saym.eventory.event.domain.EventSortType;
import com.saym.eventory.event.domain.repository.EventRepository;
import com.saym.eventory.global.s3.service.S3Service;
import com.saym.eventory.member.domain.Member;
import com.saym.eventory.member.domain.UserType;
import com.saym.eventory.member.domain.repository.MemberRepository;
import com.saym.eventory.event.api.dto.request.EventRequestDto;
import com.saym.eventory.store.api.dto.response.StoreResponseDto;
import com.saym.eventory.store.domain.Store;
import com.saym.eventory.store.domain.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final S3Service s3Service;

    private Member getMemberByPrincipal(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(Error.MEMBER_NOT_FOUND, "회원을 찾을 수 없습니다."));
    }

    // 행사 불러오기 (제목, 날짜, 사진) = 행사 조회 페이지
    public List<EventInfoResponseDto> getEventList() {
        return eventRepository.findAll().stream()
                .map(event -> new EventInfoResponseDto(
                        event.getEventId(),
                        event.getEventName(),
                        event.getEventStartDate(),
                        event.getEventEndDate(),
                        event.getPictureUrl()
                ))
                .toList();
        };

    // 행사 상세 정보(내용, 주소) 조회
    public EventDetailResponseDto getEventDetail(Long eventId) {
        return eventRepository.findById(eventId)
                .map(event -> new EventDetailResponseDto(
                        event.getEventId(),
                        event.getEventName(),
                        event.getEventStartDate(),
                        event.getEventEndDate(),
                        event.getPictureUrl(),
                        event.getContent(),
                        event.getAddress()
                ))
                .orElseThrow(() -> new RuntimeException("행사를 찾을 수 없습니다."));
    }

    // 같은 지역 가게 추천
    public List<StoreResponseDto> getRecommendedStores(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("행사를 찾을 수 없습니다."));

        String areaName = event.getArea().getName();

        List<Store> stores = storeRepository.findByAddressContains(areaName);

        return stores.stream()
                .map(StoreResponseDto::from)
                .toList();
    }

    // 북마크 추가
    @Transactional
    public void addBookmark(Long memberId, Long eventId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("행사가 존재하지 않습니다."));

        if (bookmarkRepository.existsByMemberAndEvent(member, event)) {
            throw new IllegalStateException("이미 북마크한 행사입니다.");
        }

        Bookmark bookmark = new Bookmark(member, event);
        bookmarkRepository.save(bookmark);
    }

    // 북마크 리스트 조회
    public List<EventInfoResponseDto> getBookmarks(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return bookmarkRepository.findByMember(member).stream()
                .map(bookmark -> {
                    Event event = bookmark.getEvent();
                    return new EventInfoResponseDto(
                            event.getEventId(),
                            event.getEventName(),
                            event.getEventStartDate(),
                            event.getEventEndDate(),
                            event.getPictureUrl()
                    );
                })
                .toList();
    }

    // 행사 생성
    @Transactional
    public Long createEvent(EventRequestDto eventRequestDto, Principal principal) {
        Member member = getMemberByPrincipal(principal);

        if (member.getUserType() != UserType.ORGANIZER) {
            throw new CustomException(Error.INVALID_USER_ACCESS,Error.INVALID_USER_ACCESS.getMessage());
        }

        // S3에 파일 업로드
        String pictureUrl = null;

        if (eventRequestDto.eventPicture() != null && !eventRequestDto.eventPicture().isEmpty()) {
            try {
                pictureUrl = s3Service.uploadFile(eventRequestDto.eventPicture(), "events");
            } catch (Exception e) {
                throw new CustomException(Error.FILE_UPLOAD_FAILED, "파일 업로드에 실패했습니다.");
            }
        }
        Event event = Event.builder()
                .eventName(eventRequestDto.eventName())
                .eventStartDate(eventRequestDto.eventStartDate())
                .eventEndDate(eventRequestDto.eventEndDate())
                .pictureUrl(pictureUrl)
                .area(eventRequestDto.area())
                .content(eventRequestDto.content())
                .address(eventRequestDto.address())
                .member(member)
                .build();

        return eventRepository.save(event).getEventId();
    }


    // 행사 수정
    @Transactional
    public void updateEvent(Long eventId, EventRequestDto dto, Principal principal) {
        Member member = getMemberByPrincipal(principal);

        if (member.getUserType() != UserType.ORGANIZER) {
            throw new CustomException(Error.INVALID_USER_ACCESS,Error.INVALID_USER_ACCESS.getMessage());
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("행사를 찾을 수 없습니다."));

        if (!event.getMember().getId().equals(member.getId())) {
            throw new CustomException(Error.INVALID_USER_ACCESS,Error.INVALID_USER_ACCESS.getMessage());
        }

        String newPictureUrl = event.getPictureUrl();

        // S3 기존 파일 삭제 후 새로운 파일 업로드
        if (dto.eventPicture() != null && !dto.eventPicture().isEmpty()) {
            if (event.getPictureUrl() != null) {
                s3Service.deleteFile(event.getPictureUrl());
            }

            try {
                newPictureUrl = s3Service.uploadFile(dto.eventPicture(), "events");
            } catch (Exception e) {
                throw new CustomException(Error.FILE_UPLOAD_FAILED, "파일 업로드에 실패했습니다.");
            }
        }
        event.updateEvent(dto);
        event.setPictureUrl(newPictureUrl);
    }

    // 행사 삭제
    @Transactional
    public void deleteEvent(Long eventId, Principal principal) {
        Member member = getMemberByPrincipal(principal);

        if (member.getUserType() != UserType.ORGANIZER) {
            throw new CustomException(Error.INVALID_USER_ACCESS,Error.INVALID_USER_ACCESS.getMessage());
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("행사를 찾을 수 없습니다."));

        if (!event.getMember().getId().equals(member.getId())) {
            throw new CustomException(Error.RESOURCE_NOT_OWNED,Error.RESOURCE_NOT_OWNED.getMessage());
        }

        // S3 파일 삭제
        if (event.getPictureUrl() != null) {
            s3Service.deleteFile(event.getPictureUrl());
        }

        eventRepository.delete(event);
    }

    // 필터 기능
    public List<Event> getFilteredEvents(List<Area> areas, LocalDate startDate, LocalDate endDate) {

        if (areas == null || areas.isEmpty()) {
            areas = null;
        }

        return eventRepository.findByFilters(areas, startDate, endDate);
    }

    // 정렬 기능
    public List<EventInfoResponseDto> getEvents(EventSortType sortType) {
        Sort sort = switch (sortType) {
            case NAME_ASC -> Sort.by(Sort.Direction.ASC, "eventName");
            case DATE_ASC -> Sort.by(Sort.Direction.ASC, "eventStartDate");
            case DATE_DESC -> Sort.by(Sort.Direction.DESC, "eventStartDate");
        };

        return eventRepository.findAll(sort)
                .stream()
                .map(EventInfoResponseDto::from)
                .toList();
    }
}