package io.naga.pg.global.error;

public enum ErrorCode {

    BAD_REQUEST(400, "잘못된 요청입니다"),
    ACTIVE_API_KEY_ALREADY_EXISTS(409, "이미 활성화된 API 키가 있습니다"),
    API_KEY_ALREADY_INACTIVE(409, "이미 비활성화된 API 키입니다"),
    DUPLICATE_LOGIN_ID(409, "이미 가입된 아이디입니다"),
    INVALID_LOGIN_CREDENTIALS(401, "아이디 또는 비밀번호가 올바르지 않습니다"),
    UNAUTHORIZED(401, "인증이 필요합니다"),
    PAYMENT_ALREADY_PROCESSED(409, "이미 처리된 결제입니다"),
    PAYMENT_INFO_MISMATCH(409, "결제 정보가 일치하지 않습니다"),
    NOT_FOUND_API_KEY(404, "API 키를 찾을 수 없습니다"),
    NOT_FOUND_PAYMENT(404, "결제 정보를 찾을 수 없습니다"),
    NOT_FOUND_USER(404, "사용자를 찾을 수 없습니다"),
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
