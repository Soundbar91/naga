package io.naga.common.error;

public record ErrorMessage(
    String code,
    String message,
    Object data
) {
    public static ErrorMessage of(ErrorCode errorCode) {
        return new ErrorMessage(errorCode.name(), errorCode.getMessage(), null);
    }

    public static ErrorMessage of(ErrorCode errorCode, Object data) {
        return new ErrorMessage(errorCode.name(), errorCode.getMessage(), data);
    }
}
