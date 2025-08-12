package com.saym.eventory.event.application;

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
    }

