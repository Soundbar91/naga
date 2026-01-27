package io.soundbar91.naga.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * API 응답의 결과 타입을 나타내는 Enum
 *
 * @since 1.0
 */
@Schema(description = "API 응답 결과 타입")
public enum ResultType {

    @Schema(description = "성공 응답")
    SUCCESS,

    @Schema(description = "오류 응답")
    ERROR
}
