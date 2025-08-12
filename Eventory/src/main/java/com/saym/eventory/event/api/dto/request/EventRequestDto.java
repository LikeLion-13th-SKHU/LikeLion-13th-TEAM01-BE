package com.saym.eventory.event.api.dto.request;

import java.time.LocalDate;
import com.saym.eventory.event.domain.Area;

public record EventRequestDto(
        String eventName,
        LocalDate eventStartDate,
        LocalDate eventEndDate,
        String pictureUrl,
        Area area,
        String content
) {}
