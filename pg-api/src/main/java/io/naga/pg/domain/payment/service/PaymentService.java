package io.naga.pg.domain.payment.service;

import static io.naga.common.error.ErrorCode.BAD_REQUEST;
import static io.naga.common.error.ErrorCode.NOT_FOUND_API_KEY;
import static io.naga.pg.domain.apikey.model.ApiKeyStatus.ACTIVE;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import io.naga.common.error.BusinessException;
import io.naga.pg.domain.apikey.model.ApiKey;
import io.naga.pg.domain.apikey.repository.ApiKeyRepository;
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
    public Payment requestPayment(String clientKey, String orderId, Integer amount) {
        validateRequest(clientKey, orderId, amount);

        ApiKey apiKey = apiKeyRepository.findByClientKeyAndStatus(clientKey, ACTIVE)
            .orElseThrow(() -> BusinessException.of(NOT_FOUND_API_KEY, "clientKey : " + clientKey));

        // 카드사 결제 정보 인증은 생략한다.
        String paymentKey = paymentKeyGenerator.generate();
        Payment payment = Payment.createRequested(apiKey.getUser(), orderId, amount, paymentKey);

        return paymentRepository.save(payment);
    }

    private void validateRequest(String clientKey, String orderId, Integer amount) {
        if (!StringUtils.hasText(clientKey)) {
            throw BusinessException.of(BAD_REQUEST, "clientKey is required");
        }
        if (!StringUtils.hasText(orderId)) {
            throw BusinessException.of(BAD_REQUEST, "orderId is required");
        }
        if (amount == null || amount <= 0) {
            throw BusinessException.of(BAD_REQUEST, "amount must be positive");
        }
    }
}
