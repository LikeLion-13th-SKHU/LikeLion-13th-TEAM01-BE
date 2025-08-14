package com.saym.eventory.member.api.dto.response;

import com.saym.eventory.member.domain.ApprovalStatus;
import com.saym.eventory.member.domain.Role;
import com.saym.eventory.member.domain.UserType;

public record MemberResponseDto(
        Long id,
        String name,
        String email,
        Role role,
        UserType userType,
        ApprovalStatus approvalStatus,
        String pictureUrl,
        String businessLicenseUrl
) {}
