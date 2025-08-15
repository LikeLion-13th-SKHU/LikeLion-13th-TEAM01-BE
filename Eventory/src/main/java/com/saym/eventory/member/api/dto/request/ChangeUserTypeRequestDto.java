package com.saym.eventory.member.api.dto.request;

import com.saym.eventory.member.domain.UserType;
import org.springframework.web.multipart.MultipartFile;

public record ChangeUserTypeRequestDto(
        UserType userType,           // GENERAL, ORGANIZER, OWNER
        MultipartFile businessLicenseFile    // 사업자등록증 이미지 URL, GENERAL은 null 허용
) {
}
