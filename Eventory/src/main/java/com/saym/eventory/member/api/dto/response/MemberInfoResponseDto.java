package com.saym.eventory.member.api.dto.response;

import com.saym.eventory.member.domain.Member;
import com.saym.eventory.member.domain.UserType;

public record MemberInfoResponseDto(
        String name,
        UserType userType
) {
    public static MemberInfoResponseDto from(Member member) {
        return new MemberInfoResponseDto(member.getName(), member.getUserType());
    }
}
