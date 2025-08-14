package com.saym.eventory.ai.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record AiChatResponseDto(
        String image_url,
        String chatContent,
        LocalDateTime chatDate,
        List<String> considerations,
        List<String> slogans,
        AiResultResponseDto.UserEvaluation userEvaluation
) {}