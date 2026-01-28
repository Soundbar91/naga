package io.soundbar91.naga.common.exception;

import static ch.qos.logback.classic.Level.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ch.qos.logback.classic.Level;
import io.soundbar91.naga.common.dto.ApiResponse;
import io.soundbar91.naga.common.dto.ErrorMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        return buildErrorResponse(ex.getErrorCode(), ex.getMessage(), ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return buildErrorResponse(errorCode, errorCode.getMessage(), ex);
    }

    private ResponseEntity<ApiResponse<Void>> buildErrorResponse(ErrorCode errorCode, String message, Exception ex) {
        String code = errorCode.name();

        logByLevel(errorCode.getLogLevel(), String.format("[%s] %s", code, message), ex);

        ErrorMessage errorMessage = ErrorMessage.of(code, message);
        ApiResponse<Void> response = ApiResponse.error(errorMessage);

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    private void logByLevel(Level level, String message, Exception ex) {
        switch (level.levelInt) {
            case ERROR_INT -> log.error(message, ex);
            case WARN_INT -> log.warn(message);
            case INFO_INT -> log.info(message);
            case DEBUG_INT -> log.debug(message);
        }
    }
}
