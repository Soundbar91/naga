package io.naga.pg.domain.payment.controller;

import static io.naga.common.error.ErrorCode.PAYMENT_REQUEST_FAILED;
import static org.springframework.http.HttpStatus.FOUND;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.naga.common.error.BusinessException;
import io.naga.pg.domain.payment.dto.request.PaymentRequest;
import io.naga.pg.domain.payment.model.Payment;
import io.naga.pg.domain.payment.service.PaymentService;
import io.naga.pg.domain.payment.support.PaymentRedirectUrlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentController {

    private static final String CLIENT_KEY_HEADER = "X-Client-Key";

    private final PaymentRedirectUrlBuilder paymentRedirectUrlBuilder;
    private final PaymentService paymentService;

    @PostMapping("/request")
    public ResponseEntity<Void> requestPayment(
        @RequestHeader(value = CLIENT_KEY_HEADER, required = false) String clientKey,
        @RequestBody PaymentRequest request
    ) {
        paymentRedirectUrlBuilder.validate(request.failUrl(), "failUrl");

        try {
            paymentRedirectUrlBuilder.validate(request.successUrl(), "successUrl");
            Payment payment = paymentService.requestPayment(clientKey, request.orderId(), request.amount());
            URI successUrl = paymentRedirectUrlBuilder.buildSuccessUrl(request.successUrl(), payment);
            return redirect(successUrl);
        } catch (BusinessException exception) {
            URI failUrl = paymentRedirectUrlBuilder.buildFailUrl(
                request.failUrl(),
                exception.getErrorCode(),
                request.orderId()
            );
            return redirect(failUrl);
        } catch (Exception exception) {
            log.error("결제 요청 처리 중 오류가 발생했습니다. orderId={}", request.orderId(), exception);
            URI failUrl = paymentRedirectUrlBuilder.buildFailUrl(
                request.failUrl(),
                PAYMENT_REQUEST_FAILED,
                request.orderId()
            );
            return redirect(failUrl);
        }
    }

    private ResponseEntity<Void> redirect(URI location) {
        return ResponseEntity.status(FOUND)
            .location(location)
            .build();
    }
}
