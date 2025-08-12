package com.saym.eventory.event.application;

import com.saym.eventory.event.api.dto.response.EventDetailResponseDto;
import com.saym.eventory.event.api.dto.response.EventInfoResponseDto;
import com.saym.eventory.event.domain.repository.EventRepository;
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


}




