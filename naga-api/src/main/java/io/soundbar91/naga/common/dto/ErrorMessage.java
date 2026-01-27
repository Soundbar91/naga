package io.soundbar91.naga.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * API 오류 응답 시 상세 정보를 담는 Record
 *
 * @param code 기계 판독용 오류 코드 (예: "VALIDATION_FAILED")
 * @param message 사람 판독용 오류 메시지
 * @since 1.0
 */
@Schema(description = "오류 상세 정보")
public record ErrorMessage(
        @Schema(description = "기계 판독용 오류 코드", example = "VALIDATION_FAILED")
        String code,

        @Schema(description = "사람 판독용 오류 메시지", example = "입력값 검증에 실패했습니다")
        String message
) {

    /**
     * Compact constructor: code와 message의 유효성을 검증
     *
     * @throws IllegalArgumentException code 또는 message가 null이거나 blank인 경우
     */
    public ErrorMessage {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Error code must not be null or blank");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Error message must not be null or blank");
        }
    }

    /**
     * ErrorMessage 인스턴스를 생성하는 정적 팩토리 메서드
     *
     * @param code 오류 코드
     * @param message 오류 메시지
     * @return 생성된 ErrorMessage 인스턴스
     */
    public static ErrorMessage of(String code, String message) {
        return new ErrorMessage(code, message);
    }
}
