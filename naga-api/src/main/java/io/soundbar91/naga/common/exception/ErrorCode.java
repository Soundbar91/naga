package io.soundbar91.naga.common.exception;

import org.springframework.http.HttpStatus;

import ch.qos.logback.classic.Level;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다", Level.ERROR),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final Level logLevel;
}
