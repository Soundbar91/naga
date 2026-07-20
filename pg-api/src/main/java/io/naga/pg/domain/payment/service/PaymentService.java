package io.naga.pg.domain.payment.service;

import static io.naga.common.error.ErrorCode.NOT_FOUND_API_KEY;
import static io.naga.common.error.ErrorCode.NOT_FOUND_PAYMENT;
import static io.naga.pg.domain.apikey.model.ApiKeyStatus.ACTIVE;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.naga.common.error.BusinessException;
import io.naga.pg.domain.apikey.model.ApiKey;
import io.naga.pg.domain.apikey.repository.ApiKeyRepository;
import io.naga.pg.domain.payment.dto.request.PaymentConfirmRequest;
import io.naga.pg.domain.payment.dto.request.PaymentRequest;
import io.naga.pg.domain.payment.dto.response.PaymentConfirmResponse;
import io.naga.pg.domain.payment.dto.response.PaymentResponse;
import io.naga.pg.domain.payment.model.Payment;
import io.naga.pg.domain.payment.repository.PaymentRepository;
import io.naga.pg.domain.payment.support.PaymentKeyGenerator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final ApiKeyRepository apiKeyRepository;
    private final PaymentKeyGenerator paymentKeyGenerator;
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse requestPayment(String clientKey, PaymentRequest request) {
        ApiKey apiKey = apiKeyRepository.findByClientKeyAndStatus(clientKey, ACTIVE)
            .orElseThrow(() -> BusinessException.of(NOT_FOUND_API_KEY, "clientKey : " + clientKey));

        // 카드사 결제 정보 인증은 생략한다.
        Payment payment = Payment.createRequested(
            apiKey.getUser(),
            request.orderId(),
            request.amount(),
            paymentKeyGenerator.generate()
        );
        return PaymentResponse.of(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentConfirmResponse confirmPayment(Integer userId, PaymentConfirmRequest request) {
        Payment payment = paymentRepository.findByPaymentKeyAndUserId(request.paymentKey(), userId)
            .orElseThrow(() -> BusinessException.of(NOT_FOUND_PAYMENT, "paymentKey : " + request.paymentKey()));

        payment.approve(request.orderId(), request.amount());
        return PaymentConfirmResponse.of(payment);
    }
}
