package io.naga.pg.domain.payment.dto.response;

import java.time.LocalDateTime;

import io.naga.pg.domain.payment.model.Payment;
import io.naga.pg.domain.payment.model.PaymentStatus;

public record PaymentConfirmResponse(
    String paymentKey,
    String orderId,
    Integer amount,
    PaymentStatus status,
    LocalDateTime approvedAt
) {

    public static PaymentConfirmResponse of(Payment payment) {
        return new PaymentConfirmResponse(
            payment.getPaymentKey(),
            payment.getOrderId(),
            payment.getAmount(),
            payment.getStatus(),
            payment.getApprovedAt()
        );
    }
}
