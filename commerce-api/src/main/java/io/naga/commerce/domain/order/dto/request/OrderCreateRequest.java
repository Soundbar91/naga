package io.naga.commerce.domain.order.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record OrderCreateRequest(
    @Valid
    @NotEmpty(message = "주문 상품은 1개 이상이어야 합니다.")
    List<OrderCreateItemRequest> items
) {
}
