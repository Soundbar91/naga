package io.naga.commerce.domain.payment.service;

import static io.naga.common.error.ErrorCode.NOT_FOUND_ORDER;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.naga.commerce.domain.order.model.Order;
import io.naga.commerce.domain.order.repository.OrderRepository;
import io.naga.commerce.domain.payment.client.PgPaymentClient;
import io.naga.commerce.domain.payment.dto.request.PaymentConfirmRequest;
import io.naga.commerce.domain.payment.dto.response.PaymentConfirmResponse;
import io.naga.commerce.domain.payment.model.Payment;
import io.naga.commerce.domain.payment.repository.PaymentRepository;
import io.naga.common.error.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PgPaymentClient pgPaymentClient;
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentConfirmResponse confirmPayment(Integer userId, PaymentConfirmRequest request) {
        Order order = orderRepository.findByOrderKeyAndUserId(request.orderId(), userId)
            .orElseThrow(() -> BusinessException.of(NOT_FOUND_ORDER, "orderId : " + request.orderId()));
        order.validatePayment(request.amount());

        PaymentConfirmResponse response = pgPaymentClient.confirmPayment(request);
        paymentRepository.save(Payment.createApproved(
            order,
            response.paymentKey(),
            response.amount(),
            response.status(),
            response.approvedAt()
        ));
        return response;
    }
}
