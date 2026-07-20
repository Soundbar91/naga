package io.naga.pg.domain.payment.controller;

import static org.springframework.http.HttpStatus.FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.naga.pg.domain.payment.dto.request.PaymentRequest;
import io.naga.pg.domain.payment.dto.response.PaymentResponse;
import io.naga.pg.domain.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/request")
    public ResponseEntity<Void> requestPayment(
        @RequestHeader(value = "X-Client-Key") String clientKey,
        @Valid @RequestBody PaymentRequest request
    ) {
        PaymentResponse payment = paymentService.requestPayment(clientKey, request);
        return ResponseEntity.status(FOUND).location(
            UriComponentsBuilder.fromUriString(request.successUrl())
                .queryParam("paymentKey", payment.paymentKey())
                .queryParam("orderId", payment.orderId())
                .queryParam("amount", payment.amount())
                .build()
                .encode()
                .toUri()
        ).build();
    }
}
