package io.naga.pg.domain.payment.dto.response;

import io.naga.pg.domain.payment.model.Payment;

public record PaymentResponse(
    String paymentKey,
    String orderId,
    Integer amount
) {

    public static PaymentResponse of(Payment payment) {
        return new PaymentResponse(
            payment.getPaymentKey(),
            payment.getOrderId(),
            payment.getAmount()
        );
    }
}
