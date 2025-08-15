package com.saym.eventory.event.api.dto.response;


import com.saym.eventory.event.domain.Event;

import java.time.LocalDate;

// 행사 정보 조회
public record EventInfoResponseDto(

        Long eventId,
        String eventName,
        LocalDate eventStartDate,
        LocalDate eventEndDate,
        String pictureUrl

) {

    public static EventInfoResponseDto from(Event event) {
        return new EventInfoResponseDto(
                event.getEventId(),
                event.getEventName(),
                event.getEventStartDate(),
                event.getEventEndDate(),
                event.getPictureUrl()
        );
    }
}
