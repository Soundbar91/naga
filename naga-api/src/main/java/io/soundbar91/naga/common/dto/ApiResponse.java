package io.soundbar91.naga.common.dto;

import static io.soundbar91.naga.common.dto.ResultType.ERROR;
import static io.soundbar91.naga.common.dto.ResultType.SUCCESS;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    ResultType result,
    T data,
    ErrorMessage error
) {
    public ApiResponse {
        if (result == SUCCESS && error != null) {
            throw new IllegalArgumentException("성공 응답은 에러 정보를 포함할 수 없습니다");
        }
        if (result == ERROR && data != null) {
            throw new IllegalArgumentException("에러 응답은 데이터를 포함할 수 없습니다");
        }
        if (result == ERROR && error == null) {
            throw new IllegalArgumentException("에러 응답은 반드시 에러 정보를 포함해야 합니다");
        }
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS, data, null);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(SUCCESS, null, null);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(ERROR, null, ErrorMessage.of(code, message));
    }

    public static <T> ApiResponse<T> error(ErrorMessage error) {
        return new ApiResponse<>(ERROR, null, error);
    }
}
