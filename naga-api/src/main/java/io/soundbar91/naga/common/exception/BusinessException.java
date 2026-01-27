package io.soundbar91.naga.common.exception;

/**
 * 비즈니스 로직에서 발생하는 모든 커스텀 예외의 기본 클래스
 *
 * <p>RuntimeException을 상속하여 체크되지 않은 예외로 동작하며,
 * ErrorCode를 통해 오류 정보를 표준화합니다.</p>
 *
 * <h3>사용 예시:</h3>
 * <pre>{@code
 * // ErrorCode만 사용
 * throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
 *
 * // ErrorCode + 커스텀 메시지
 * throw new BusinessException(ErrorCode.VALIDATION_FAILED, "Email format invalid");
 * }</pre>
 *
 * @since 1.0
 */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * ErrorCode만으로 예외를 생성합니다
     *
     * @param errorCode 오류 코드
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode와 커스텀 메시지로 예외를 생성합니다
     *
     * <p>ErrorCode의 기본 메시지를 오버라이드하여 더 구체적인 정보를 제공할 때 사용합니다.</p>
     *
     * @param errorCode 오류 코드
     * @param message 커스텀 오류 메시지
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 오류 코드를 반환합니다
     *
     * @return 오류 코드
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
