package com.saym.eventory.event.application;

import com.saym.eventory.event.api.dto.request.EventRequestDto;
import com.saym.eventory.event.api.dto.response.EventInfoResponseDto;
import com.saym.eventory.event.domain.Event;
import com.saym.eventory.event.domain.repository.EventRepository;
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