package com.saym.eventory.global.oauth2.kakao.dto;

public record TokenDto(
        String accessToken,
        String refreshToken
) {
    public static TokenDto of(final String accessToken, final String refreshToken) {
        return new TokenDto(accessToken, refreshToken);
    }

}
