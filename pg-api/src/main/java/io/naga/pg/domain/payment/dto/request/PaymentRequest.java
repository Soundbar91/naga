package io.naga.pg.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequest(
    @NotBlank(message = "주문 ID는 필수입니다.")
    String orderId,

    @NotNull(message = "결제 금액은 필수입니다.")
    @Positive(message = "결제 금액은 양수여야 합니다.")
    Integer amount,

    @NotBlank(message = "성공 URL은 필수입니다.")
    String successUrl
) {
}
