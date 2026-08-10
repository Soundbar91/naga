package io.naga.pg.global.error;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String detail;

    public BusinessException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = detail;
    }

    public static BusinessException of(ErrorCode errorCode, String detail) {
        return new BusinessException(errorCode, detail);
    }

}
