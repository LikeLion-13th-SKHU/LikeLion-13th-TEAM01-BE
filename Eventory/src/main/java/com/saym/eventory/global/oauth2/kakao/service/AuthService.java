package com.saym.eventory.global.oauth2.kakao.service;

import com.saym.eventory.common.exception.CustomException;
import com.saym.eventory.common.exception.Error;
import com.saym.eventory.global.oauth2.kakao.domain.LoginResult;
import com.saym.eventory.global.oauth2.kakao.domain.RefreshToken;
import com.saym.eventory.global.oauth2.kakao.dto.AuthResponseDto;
import com.saym.eventory.global.oauth2.kakao.dto.TokenDto;
import com.saym.eventory.global.token.TokenProvider;
import com.saym.eventory.member.application.MemberService;
import com.saym.eventory.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final KakaoService kakaoService;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;


    @Transactional
    public AuthResponseDto signUpOrSignIn(String authorizationCode) {
        LoginResult result = null;

        String accessToken = kakaoService.getAccessToken(authorizationCode);
        result = kakaoService.loginOrSignUp(accessToken);

        if (result == null || result.member() == null) {
            throw new CustomException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage());
        }

        Member member = result.member();
        TokenDto tokenDto = tokenProvider.createToken(member);

        if (tokenDto == null) {
            throw new CustomException(Error.JWT_CREATION_EXCEPTION, Error.JWT_CREATION_EXCEPTION.getMessage());
        }

        return AuthResponseDto.builder()
                .accessToken(tokenDto.accessToken())
                .refreshToken(tokenDto.refreshToken())
                .email(member.getEmail())
                .name(member.getName())
                .userType(member.getUserType())
                .approvalStatus(member.getApprovalStatus())
                .build();
    }

    @Transactional
    public TokenDto reIssueToken(RefreshToken refreshToken) {
        Member member = memberService.findByRefreshToken(refreshToken.refreshToken());
        return tokenProvider.reIssueTokenByRefresh(member, refreshToken.refreshToken());
    }
}