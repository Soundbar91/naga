package io.naga.commerce.domain.order.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    CREATED("주문 생성"),
    PAYMENT_PENDING("결제 대기"),
    PAID("결제 완료"),
    CANCELED("주문 취소"),
    FAILED("주문 실패"),
    ;

    private final String description;
}
