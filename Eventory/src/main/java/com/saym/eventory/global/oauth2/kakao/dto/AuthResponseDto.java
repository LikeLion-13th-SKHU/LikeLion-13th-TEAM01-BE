package com.saym.eventory.global.oauth2.kakao.dto;

import com.saym.eventory.member.domain.UserType;
import com.saym.eventory.member.domain.ApprovalStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String name;
    private UserType userType;
    private ApprovalStatus approvalStatus;
}