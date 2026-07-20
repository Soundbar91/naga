package io.naga.commerce.domain.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.naga.commerce.domain.payment.dto.request.PaymentConfirmRequest;
import io.naga.commerce.domain.payment.dto.response.PaymentConfirmResponse;
import io.naga.commerce.domain.payment.service.PaymentService;
import io.naga.commerce.global.auth.UserId;
import io.naga.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<PaymentConfirmResponse>> confirmPayment(
        @UserId Integer userId,
        @Valid @RequestBody PaymentConfirmRequest request
    ) {
        PaymentConfirmResponse response = paymentService.confirmPayment(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
