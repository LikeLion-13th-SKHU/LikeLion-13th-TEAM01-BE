package com.saym.eventory.ai.api.dto.response;

import java.time.LocalDateTime;

public record AiAllRecordResponseDto(
        Long aiId,
        String imageUrl,
        String chatContent,
        LocalDateTime chatDate
) {
}
