package com.saym.eventory.global.config;

import com.saym.eventory.global.jwt.JwtFilter;
import com.saym.eventory.global.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration  // 하나 이상의 @Bean 메서드를 가지고 있어 spring 컨테이너가 해당 메서드들을 관리하고 빈으로 등록
@EnableWebSecurity  // spring security 활성화, 웹 보안 설정 제공
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    // 요청에 대한 인증 및 권한 부여 정의
    // Http 요청에 대해 다양한 보안 필터들을 순차적으로 적용하는 구조
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 HTTP 인증 비활성화. 기본 인증은 보안에 취약할 수 있어 주로 비활성화하는 편.
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 세션 관리 정책(서버측에서 세션 생성 X. 클라이언트에서 jwt 토큰을 사용하여 인증하도록 함)
                .formLogin(AbstractHttpConfigurer::disable) // 스프링의 기본 로그인 폼을 사용하지 않고, auth 등의 외부 인증 방식을 사용하기 위해 비활성화.
                .logout(AbstractHttpConfigurer::disable) // 로그아웃 기능 비활성화
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll() // Swagger API 문서 허용
                        .requestMatchers("api/v1/oauth2/**").permitAll() // OAuth2 허용
                        .requestMatchers("api/v1/event/**").permitAll() // event 허용

                        .anyRequest().authenticated() // 그 외 모든 요청은 인증이 필요하도록 설정
                ) // 인증 및 권한 부여 규칙 설정
                .cors(cors -> cors.configurationSource(configurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                // jwt 토큰 인증을 위한 jwtfilter 추가
                .build();
    }

    // CORS 설정을 정의하는 메서드.
    // CORS는 다른 도메인에서 리소스를 요청할 때 이를 허용할 지 여부를 결정하는 매커니즘
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("https://book-hub.store","https://eventory-azure.vercel.app/","http://localhost:5173"));
//        configuration.setAllowedOriginPatterns(List.of("*")); // 모든 도메인에서의 요청 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // HTTP 에 대한 요청 허용
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 요청 허용
        configuration.setExposedHeaders(List.of("Access-Control-Allow-Credentials", "Authorization", "Set-Cookie")); // 특정 응답 헤더를 클라이언트가 접근할 수 있도록 노출
        configuration.setAllowCredentials(true); // 자격 증명(쿠기 등)을 포함한 요청 허용
        configuration.setMaxAge(3600L); // 브라우저가 사전 요청을 캐시할 시간 1시간으로 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}