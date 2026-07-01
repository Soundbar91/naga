package io.naga.common.response;

import static io.naga.common.response.ResultType.ERROR;
import static io.naga.common.response.ResultType.SUCCESS;

import io.naga.common.error.ErrorCode;
import io.naga.common.error.ErrorMessage;

public record ApiResponse<T>(
    ResultType result,
    T data,
    ErrorMessage error
) {

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(SUCCESS, null, null);
    }

    public static <S> ApiResponse<S> success(S data) {
        return new ApiResponse<>(SUCCESS, data, null);
    }

    public static ApiResponse<Void> error(ErrorCode errorCode) {
        return new ApiResponse<>(ERROR, null, ErrorMessage.of(errorCode, null));
    }

    public static <S> ApiResponse<S> error(ErrorCode errorCode, Object errorData) {
        return new ApiResponse<>(ERROR, null, ErrorMessage.of(errorCode, errorData));
    }
}
