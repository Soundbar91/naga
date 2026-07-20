package io.naga.commerce.domain.order.dto.response;

import io.naga.commerce.domain.order.model.Order;

public record OrderCreateResponse(
    String orderId
) {

    public static OrderCreateResponse of(Order order) {
        return new OrderCreateResponse(order.getOrderKey());
    }
}
