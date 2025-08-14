package com.saym.eventory.ai.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AiResultResponseDto(
        String title,
        List<String> considerations,
        List<String> slogans,

        @JsonProperty("user_evaluation")
        UserEvaluation userEvaluation
) {
    public record UserEvaluation(
            @JsonProperty("positive_percentage")
            String positivePercentage,

            @JsonProperty("negative_reasons")
            List<String> negativeReasons
    ) {}
}