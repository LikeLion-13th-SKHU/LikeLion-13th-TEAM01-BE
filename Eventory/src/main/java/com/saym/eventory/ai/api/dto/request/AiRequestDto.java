package com.saym.eventory.ai.api.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record AiRequestDto(
        MultipartFile imageFile,
//        String title,
        String description
) {}
