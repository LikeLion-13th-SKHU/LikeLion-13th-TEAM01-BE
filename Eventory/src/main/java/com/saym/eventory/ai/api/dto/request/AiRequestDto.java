package com.saym.eventory.ai.api.dto.request;

public record AiRequestDto(
        String image_url,
        String title,
        String description
) {}
