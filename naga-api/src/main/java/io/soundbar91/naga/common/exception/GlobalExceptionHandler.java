package io.soundbar91.naga.common.exception;

import ch.qos.logback.classic.Level;
import io.soundbar91.naga.common.dto.ApiResponse;
import io.soundbar91.naga.common.dto.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 애플리케이션 전역 예외를 처리하는 핸들러
 *
 * <p>BusinessException을 catch하여 일관된 ApiResponse<Void> 형식으로 변환하고,
 * ErrorCode의 로그 레벨에 따라 적절하게 로깅합니다.</p>
 *
 * <h3>로깅 전략:</h3>
 * <ul>
 *   <li>ERROR 레벨: 스택 트레이스 포함하여 로깅</li>
 *   <li>WARN 레벨: 메시지만 로깅</li>
 *   <li>INFO 레벨: 메시지만 로깅</li>
 *   <li>DEBUG 레벨: 메시지만 로깅</li>
 * </ul>
 *
 * <h3>응답 형식:</h3>
 * <pre>{@code
 * {
 *   "result": "ERROR",
 *   "data": null,
 *   "error": {
 *     "code": "RESOURCE_NOT_FOUND",
 *     "message": "요청한 리소스를 찾을 수 없습니다"
 *   }
 * }
 * }</pre>
 *
 * @since 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * BusinessException을 처리하여 일관된 오류 응답을 반환합니다
     *
     * <p>ErrorCode의 로그 레벨에 따라 적절하게 로깅하며,
     * ApiResponse<Void> 형식으로 변환하여 반환합니다.</p>
     *
     * @param ex 발생한 BusinessException
     * @return 표준화된 오류 응답
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        String message = ex.getMessage();
        String code = errorCode.getCode();

        // ErrorCode의 로그 레벨에 따라 로깅
        logByLevel(errorCode.getLogLevel(),
                String.format("[%s] %s", code, message),
                ex);

        // ApiResponse<Void> 형식으로 반환
        ErrorMessage errorMessage = new ErrorMessage(code, message);
        ApiResponse<Void> response = ApiResponse.error(errorMessage);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }

    /**
     * 로그 레벨에 따라 적절하게 로깅합니다
     *
     * <p>ERROR 레벨인 경우에만 스택 트레이스를 포함하여 로깅하며,
     * 그 외의 레벨에서는 메시지만 로깅합니다.</p>
     *
     * @param level 로그 레벨
     * @param message 로그 메시지
     * @param ex 예외 객체
     */
    private void logByLevel(Level level, String message, Exception ex) {
        if (level == Level.ERROR) {
            log.error(message, ex);  // 스택 트레이스 포함
        } else if (level == Level.WARN) {
            log.warn(message);
        } else if (level == Level.INFO) {
            log.info(message);
        } else if (level == Level.DEBUG) {
            log.debug(message);
        }
    }
}
