package com.saym.eventory.event.api.dto.request;

import java.time.LocalDate;
import com.saym.eventory.event.domain.Area;
import org.springframework.web.multipart.MultipartFile;

public record EventRequestDto(
        String eventName,
        LocalDate eventStartDate,
        LocalDate eventEndDate,
        MultipartFile eventPicture,
        Area area,
        String content,
        String address
) {}

