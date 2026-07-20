package io.naga.commerce.domain.payment.client;

import static io.naga.common.error.ErrorCode.PAYMENT_REQUEST_FAILED;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import io.naga.commerce.domain.payment.dto.request.PaymentConfirmRequest;
import io.naga.commerce.domain.payment.dto.response.PaymentConfirmResponse;
import io.naga.common.error.BusinessException;
import io.naga.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PgPaymentClient {

    private final RestClient pgRestClient;

    public PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request) {
        try {
            ApiResponse<PaymentConfirmResponse> response = pgRestClient.post()
                .uri("/v1/payments/confirm")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

            if (response == null || response.data() == null) {
                throw paymentRequestFailed();
            }
            return response.data();
        } catch (RestClientException exception) {
            throw paymentRequestFailed();
        }
    }

    private BusinessException paymentRequestFailed() {
        return BusinessException.of(PAYMENT_REQUEST_FAILED, "PG payment confirmation failed");
    }
}
