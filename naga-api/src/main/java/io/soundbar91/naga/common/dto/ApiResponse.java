package io.soundbar91.naga.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 모든 REST API 엔드포인트에서 사용하는 공통 응답 래퍼
 *
 * <p>성공 응답과 오류 응답을 명확히 구분하며, 타입 안전한 API를 제공합니다.
 * JSON 직렬화 시 null 필드는 제외됩니다.</p>
 *
 * <h3>사용 예시:</h3>
 * <pre>{@code
 * // 데이터가 있는 성공 응답
 * ApiResponse<UserDto> response = ApiResponse.success(userDto);
 *
 * // 데이터가 없는 성공 응답 (예: 삭제 작업)
 * ApiResponse<Void> response = ApiResponse.success();
 *
 * // 오류 응답
 * ApiResponse<Void> response = ApiResponse.error("NOT_FOUND", "사용자를 찾을 수 없습니다");
 * }</pre>
 *
 * @param <T> 응답 데이터의 타입
 * @param result 응답 결과 타입 (SUCCESS 또는 ERROR)
 * @param data 성공 시 반환할 데이터 (ERROR일 경우 null)
 * @param error 오류 시 상세 정보 (SUCCESS일 경우 null)
 * @since 1.0
 */
@Schema(description = "API 공통 응답 래퍼")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        @Schema(description = "응답 결과 타입", example = "SUCCESS")
        ResultType result,

        @Schema(description = "성공 시 반환 데이터 (ERROR일 경우 null)")
        T data,

        @Schema(description = "오류 시 상세 정보 (SUCCESS일 경우 null)")
        ErrorMessage error
) {

    /**
     * Compact constructor: 불변 조건을 검증
     *
     * <ul>
     *   <li>SUCCESS 응답은 error가 null이어야 함</li>
     *   <li>ERROR 응답은 data가 null이어야 함</li>
     *   <li>ERROR 응답은 error가 반드시 있어야 함</li>
     * </ul>
     *
     * @throws IllegalArgumentException 불변 조건을 위반하는 경우
     */
    public ApiResponse {
        if (result == ResultType.SUCCESS && error != null) {
            throw new IllegalArgumentException("Success response cannot have error");
        }
        if (result == ResultType.ERROR && data != null) {
            throw new IllegalArgumentException("Error response cannot have data");
        }
        if (result == ResultType.ERROR && error == null) {
            throw new IllegalArgumentException("Error response must have error details");
        }
    }

    /**
     * 데이터가 있는 성공 응답을 생성하는 정적 팩토리 메서드
     *
     * @param <T> 응답 데이터 타입
     * @param data 반환할 데이터
     * @return 성공 응답 인스턴스
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    /**
     * 데이터가 없는 성공 응답을 생성하는 정적 팩토리 메서드
     *
     * <p>주로 삭제, 수정 등 반환할 데이터가 없는 작업에 사용</p>
     *
     * @param <T> 응답 데이터 타입 (Void 사용 권장)
     * @return 성공 응답 인스턴스
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    /**
     * 오류 응답을 생성하는 정적 팩토리 메서드
     *
     * @param <T> 응답 데이터 타입 (Void 사용 권장)
     * @param code 기계 판독용 오류 코드
     * @param message 사람 판독용 오류 메시지
     * @return 오류 응답 인스턴스
     */
    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(code, message));
    }

    /**
     * ErrorMessage를 받아 오류 응답을 생성하는 정적 팩토리 메서드
     *
     * @param <T> 응답 데이터 타입 (Void 사용 권장)
     * @param error 오류 상세 정보
     * @return 오류 응답 인스턴스
     */
    public static <T> ApiResponse<T> error(ErrorMessage error) {
        return new ApiResponse<>(ResultType.ERROR, null, error);
    }
}
