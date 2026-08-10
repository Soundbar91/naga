package io.naga.commerce.global.error;

public enum ErrorCode {

    BAD_REQUEST(400, "잘못된 요청입니다"),
    DUPLICATE_LOGIN_ID(409, "이미 가입된 아이디입니다"),
    INVALID_LOGIN_CREDENTIALS(401, "아이디 또는 비밀번호가 올바르지 않습니다"),
    UNAUTHORIZED(401, "인증이 필요합니다"),
    PAYMENT_REQUEST_FAILED(500, "결제 요청 처리에 실패했습니다"),
    NOT_FOUND_ORDER(404, "주문을 찾을 수 없습니다"),
    NOT_FOUND_USER(404, "사용자를 찾을 수 없습니다"),
    NOT_FOUND_PRODUCT(404, "상품을 찾을 수 없습니다"),
    ORDER_AMOUNT_MISMATCH(409, "주문 금액이 일치하지 않습니다"),
    INVALID_ORDER_STATUS(409, "결제할 수 없는 주문 상태입니다"),
    PRODUCT_MISMATCH_IN_ORDER(409, "주문 상품 정보가 일치하지 않습니다"),
    NOT_SALE_PRODUCT(409, "판매 중인 상품이 아닙니다"),
    OUT_OF_STOCK(409, "상품 재고가 부족합니다"),
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
