package com.saym.eventory.global.oauth2.kakao.controller;

import com.saym.eventory.common.template.RspTemplate;
import com.saym.eventory.global.oauth2.kakao.domain.RefreshToken;
import com.saym.eventory.global.oauth2.kakao.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
@Slf4j
@Tag(name = "카카오 로그인 API", description = "카카오 로그인 관련 API들 입니다.")
public class KakaoLoginController {

    private final AuthService authService;

    // 소셜 로그인 회원가입 및 로그인
    @GetMapping
    @Operation(method = "GET", summary = "카카오 로그인 회원가입 및 로그인", description = "소셜 로그인을 진행해 회원가입 및 로그인을 진행합니다. 소셜 로그인은 자체 회원가입으로 넘어갑니다. 여기서 넘겨주는 토큰을 internal/join(회원가입)으로 넘겨주세요.")
    public RspTemplate<?> kakaoLogin(
            @Parameter(description = "인가코드", in = ParameterIn.QUERY)
            @RequestParam("code") String socialAccessToken
    ) {
        return RspTemplate.success(HttpStatus.OK, "로그인 성공",authService.signUpOrSignIn(socialAccessToken));
    }

    @PostMapping("/token")
    @Operation(method = "POST", description = "리프레시토큰을 통해 엑세스, 리프레시토큰을 발급받습니다.")
    public RspTemplate<?> reIssueToken(@RequestBody RefreshToken token) {
        return RspTemplate.success(HttpStatus.OK ,"리프레시토큰 발급 성공",authService.reIssueToken(token));
    }
}