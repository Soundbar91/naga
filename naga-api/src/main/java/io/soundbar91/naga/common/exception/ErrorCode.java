package io.soundbar91.naga.common.exception;

import org.springframework.http.HttpStatus;

import ch.qos.logback.classic.Level;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다", Level.ERROR),

    // User
    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다", Level.INFO),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다", Level.INFO),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final Level logLevel;
}
