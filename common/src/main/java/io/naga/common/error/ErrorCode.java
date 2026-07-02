package io.naga.common.error;

public enum ErrorCode {

    DUPLICATE_LOGIN_ID(409, "이미 가입된 아이디입니다"),
    ;

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
