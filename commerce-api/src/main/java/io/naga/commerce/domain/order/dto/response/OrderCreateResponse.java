package io.naga.commerce.domain.order.dto.response;

import io.naga.commerce.domain.order.model.Order;

public record OrderCreateResponse(
    Integer orderId
) {

    public static OrderCreateResponse of(Order order) {
        return new OrderCreateResponse(order.getId());
    }
}
