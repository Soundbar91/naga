package io.naga.commerce.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 가입된 아이디입니다"),
    INVALID_LOGIN_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다"),
    PAYMENT_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "결제 요청 처리에 실패했습니다"),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다"),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다"),
    ORDER_AMOUNT_MISMATCH(HttpStatus.CONFLICT, "주문 금액이 일치하지 않습니다"),
    INVALID_ORDER_STATUS(HttpStatus.CONFLICT, "결제할 수 없는 주문 상태입니다"),
    PRODUCT_MISMATCH_IN_ORDER(HttpStatus.CONFLICT, "주문 상품 정보가 일치하지 않습니다"),
    NOT_SALE_PRODUCT(HttpStatus.CONFLICT, "판매 중인 상품이 아닙니다"),
    OUT_OF_STOCK(HttpStatus.CONFLICT, "상품 재고가 부족합니다"),
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
