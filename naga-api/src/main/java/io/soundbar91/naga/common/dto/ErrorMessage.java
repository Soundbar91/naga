package io.soundbar91.naga.common.dto;

public record ErrorMessage(
    String code,
    String message
) {
    public ErrorMessage {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("에러 코드는 null이거나 공백일 수 없습니다");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("에러 메시지는 null이거나 공백일 수 없습니다");
        }
    }

    public static ErrorMessage of(String code, String message) {
        return new ErrorMessage(code, message);
    }
}
