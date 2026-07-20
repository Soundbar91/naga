package io.naga.pg.domain.payment.dto.request;

public record PaymentRequest(
    String orderId,
    Integer amount,
    String successUrl,
    String failUrl
) {

}
