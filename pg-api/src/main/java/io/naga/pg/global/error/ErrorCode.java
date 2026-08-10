package io.naga.pg.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
    ACTIVE_API_KEY_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 활성화된 API 키가 있습니다"),
    API_KEY_ALREADY_INACTIVE(HttpStatus.CONFLICT, "이미 비활성화된 API 키입니다"),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 가입된 아이디입니다"),
    INVALID_LOGIN_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다"),
    PAYMENT_ALREADY_PROCESSED(HttpStatus.CONFLICT, "이미 처리된 결제입니다"),
    PAYMENT_INFO_MISMATCH(HttpStatus.CONFLICT, "결제 정보가 일치하지 않습니다"),
    NOT_FOUND_API_KEY(HttpStatus.NOT_FOUND, "API 키를 찾을 수 없습니다"),
    NOT_FOUND_PAYMENT(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다"),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
