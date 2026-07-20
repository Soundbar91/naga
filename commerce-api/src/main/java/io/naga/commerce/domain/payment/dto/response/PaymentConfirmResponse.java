package io.naga.commerce.domain.payment.dto.response;

import java.time.LocalDateTime;

public record PaymentConfirmResponse(
    String paymentKey,
    String orderId,
    Integer amount,
    String status,
    LocalDateTime approvedAt
) {
}
