package com.saym.eventory.global.oauth2.kakao.domain;

import com.saym.eventory.member.domain.Member;

public record LoginResult(Member member) {
    public static LoginResult from(final Member member){
        return new LoginResult(member);
    }
}