package com.saym.eventory.ai.api.dto.response;

public record AiAnalyzeResponseDto(
        Long aiId,
        AiResultResponseDto aiResult
) {
}
