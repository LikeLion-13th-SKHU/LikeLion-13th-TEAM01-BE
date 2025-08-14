package com.saym.eventory.event.api.dto.response;


import java.time.LocalDate;

// 행사 정보 조회
public record EventInfoResponseDto(

        Long eventId,
        String eventName,
        LocalDate eventStartDate,
        LocalDate eventEndDate,
        String pictureUrl

) {}
