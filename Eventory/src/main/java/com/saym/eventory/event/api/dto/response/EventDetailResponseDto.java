package com.saym.eventory.event.api.dto.response;

import java.time.LocalDate;

// 행사 상세 정보 조회
public record EventDetailResponseDto(

        Long eventId,
        String eventName,
        LocalDate eventStartDate,
        LocalDate eventEndDate,
        String pictureUrl,
        String content,
        String addless

) {
}
