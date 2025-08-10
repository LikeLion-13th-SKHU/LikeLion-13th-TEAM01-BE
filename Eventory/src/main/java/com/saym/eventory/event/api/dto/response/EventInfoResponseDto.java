package com.saym.eventory.event.api.dto.response;


// 여기... 행사 조회...
public record EventInfoResponseDto(

        Long eventId,
        String eventName,
        String eventStartDate,
        String eventEndDate,
        String pictureUrl

) {}
