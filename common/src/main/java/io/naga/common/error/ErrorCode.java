package io.naga.common.error;

public enum ErrorCode {

    DUPLICATE_LOGIN_ID(409, "이미 가입된 아이디입니다"),
    INVALID_LOGIN_CREDENTIALS(401, "아이디 또는 비밀번호가 올바르지 않습니다"),
    NOT_FOUND_PRODUCT(404, "상품을 찾을 수 없습니다"),
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
