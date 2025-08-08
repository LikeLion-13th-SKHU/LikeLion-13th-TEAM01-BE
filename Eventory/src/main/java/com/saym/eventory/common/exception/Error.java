package com.saym.eventory.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Error {

    // 404 NOT FOUND
    NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "찾을 수 없는 유저입니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "리프레시 토큰이 존재하지 않습니다."),

    // community
    NOT_FOUND_COMMUNITY(HttpStatus.NOT_FOUND, "존재하지 않는 커뮤니티 글입니다."),

    // bookmark
    BOOKMARK_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 북마크한 글입니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크가 존재하지 않습니다."),

    // Wish
    WISH_NOT_FOUND(HttpStatus.NOT_FOUND, "독서록을 찾을 수 없습니다."),

    // 400 BAD REQUEST EXCEPTION
    BAD_REQUEST_ID(HttpStatus.BAD_REQUEST, "잘못된 id값입니다."),
    BAD_REQUEST_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    BAD_REQUEST_VALIDATION(HttpStatus.BAD_REQUEST, "유효한 값으로 요청을 다시 보내주세요."),
    BAD_REQUEST_PROVIDER(HttpStatus.BAD_REQUEST, "다른 유형 로그인으로 가입하셨습니다."),
    BAD_REQUEST_EMAIL(HttpStatus.BAD_REQUEST, "사용할 수 없는 이메일입니다."),
    EXIST_USER(HttpStatus.BAD_REQUEST, "이미 가입한 유저입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다."),

    // 401 UNAUTHORIZED EXCEPTION
    TOKEN_TIME_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    VERIFY_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 인증입니다. 재발송 해주세요."),
    REFRESH_TOKEN_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다. 재로그인해주세요."),
    EXPIRED_APPLE_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 아이덴티티 토큰입니다."),
    UN_AUTHORIZED(HttpStatus.UNAUTHORIZED, "토큰 인증을 안한 유저입니다."),

    // 403 FORBIDDEN EXCEPTION
    FORBIDDEN(HttpStatus.FORBIDDEN, "리소스에 대한 권한이 없습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN,"리소스에 접근할 수 없는 유저입니다. 토큰을 확인해주세요."),
    NO_INSTRUCTOR(HttpStatus.FORBIDDEN, "강사정보가 없는 유저입니다."),
    INVALID_USER_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없는 유저입니다."),

    // 422 UNPROCESSABLE_ENTITY
    UNPROCESSABLE_ENTITY_DELETE_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "서버에서 요청을 이해해 삭제하려는 도중 문제가 생겼습니다."),
    UNPROCESSABLE_KAKAO_SERVER_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "카카오서버와 통신 중 오류가 발생했습니다."),
    INVALID_KAKAO_ACCOUNT_INFO(HttpStatus.UNPROCESSABLE_ENTITY, "카카오 계정 정보가 유효하지 않습니다."),
    KAKAO_AUTH_SERVER_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "카카오 인증 서버 호출에 실패했습니다."),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생했습니다"),
    JWT_CREATION_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "JWT 토큰 생성 중 오류가 발생했습니다."),
    DATABASE_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "DB 저장 중 오류가 발생했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getErrorCode() {
        return httpStatus.value();
    }
}