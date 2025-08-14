package com.saym.eventory.event.application;


import com.saym.eventory.bookmark.domain.Bookmark;
import com.saym.eventory.bookmark.domain.repository.BookmarkRepository;
import com.saym.eventory.event.api.dto.response.EventDetailResponseDto;
import com.saym.eventory.event.api.dto.response.EventInfoResponseDto;
import com.saym.eventory.event.domain.Event;
import com.saym.eventory.event.domain.repository.EventRepository;
import com.saym.eventory.member.domain.Member;
import com.saym.eventory.member.domain.repository.MemberRepository;
import com.saym.eventory.event.api.dto.request.EventRequestDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;


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
    public Long createEvent(EventRequestDto eventRequestDto) {
        Event event = Event.builder()
                .eventName(eventRequestDto.eventName())
                .eventStartDate(eventRequestDto.eventStartDate())
                .eventEndDate(eventRequestDto.eventEndDate())
                .pictureUrl(eventRequestDto.pictureUrl())
                .area(eventRequestDto.area())
                .content(eventRequestDto.content())
                .build();
        return eventRepository.save(event).getEventId();
    }

    // 행사 수정
    @Transactional
    public void updateEvent(Long eventId, EventRequestDto dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("행사를 찾을 수 없습니다."));
        event.updateEvent(dto);
    }

    // 행사 삭제
    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("행사를 찾을 수 없습니다."));
        eventRepository.delete(event);
    }
}