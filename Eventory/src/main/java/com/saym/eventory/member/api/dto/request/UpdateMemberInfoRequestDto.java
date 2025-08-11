package com.saym.eventory.member.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateMemberInfoRequestDto(
        @NotBlank(message = "이름은 필수 입력입니다.")
        String name
) {}