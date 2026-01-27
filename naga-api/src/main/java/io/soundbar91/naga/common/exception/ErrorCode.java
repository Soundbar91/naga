package io.soundbar91.naga.common.exception;

import ch.qos.logback.classic.Level;
import org.springframework.http.HttpStatus;

/**
 * 애플리케이션의 모든 오류 코드를 중앙 집중식으로 관리하는 Enum
 *
 * <p>각 오류 코드는 HTTP 상태 코드, 사람이 읽을 수 있는 메시지, 로그 레벨을 포함합니다.
 * 카테고리별로 그룹화되어 있습니다: COMMON, AUTH, VALIDATION, RESOURCE, BUSINESS, DATABASE</p>
 *
 * <h3>사용 예시:</h3>
 * <pre>{@code
 * throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
 * throw new BusinessException(ErrorCode.VALIDATION_FAILED, "Email format invalid");
 * }</pre>
 *
 * @since 1.0
 */
public enum ErrorCode {

    // COMMON: 공통 오류
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다", Level.WARN),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다", Level.ERROR),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다", Level.WARN),

    // AUTH: 인증/인가
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다", Level.INFO),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다", Level.INFO),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다", Level.INFO),

    // VALIDATION: 입력 검증
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "입력값 검증에 실패했습니다", Level.WARN),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "입력값 형식이 올바르지 않습니다", Level.WARN),

    // RESOURCE: 리소스 관리
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다", Level.INFO),
    RESOURCE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다", Level.INFO),

    // BUSINESS: 비즈니스 로직
    BUSINESS_RULE_VIOLATION(HttpStatus.BAD_REQUEST, "비즈니스 규칙을 위반했습니다", Level.WARN),

    // DATABASE: 데이터베이스
    DATABASE_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 연결 오류가 발생했습니다", Level.ERROR),
    DATABASE_CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "데이터베이스 제약 조건을 위반했습니다", Level.WARN);

    private final HttpStatus httpStatus;
    private final String message;
    private final Level logLevel;

    /**
     * ErrorCode 생성자
     *
     * @param httpStatus HTTP 응답 상태 코드
     * @param message 사람이 읽을 수 있는 오류 메시지
     * @param logLevel 로깅 수준
     */
    ErrorCode(HttpStatus httpStatus, String message, Level logLevel) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.logLevel = logLevel;
    }

    /**
     * HTTP 상태 코드를 반환합니다
     *
     * @return HTTP 상태 코드
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * 사람이 읽을 수 있는 오류 메시지를 반환합니다
     *
     * @return 오류 메시지
     */
    public String getMessage() {
        return message;
    }

    /**
     * 로그 레벨을 반환합니다
     *
     * @return 로그 레벨
     */
    public Level getLogLevel() {
        return logLevel;
    }

    /**
     * 오류 코드 문자열을 반환합니다 (enum 상수 이름)
     *
     * @return 오류 코드
     */
    public String getCode() {
        return this.name();
    }
}
