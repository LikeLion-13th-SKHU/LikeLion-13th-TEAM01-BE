package com.saym.eventory.member.api.dto.request;

import com.saym.eventory.member.domain.UserType;

public record ChangeUserTypeRequestDto(
        UserType userType,           // GENERAL, ORGANIZER, OWNER
        String businessLicenseUrl    // 사업자등록증 이미지 URL, GENERAL은 null 허용
) {
}
