package io.naga.pg.domain.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.naga.pg.domain.payment.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Optional<Payment> findByPaymentKeyAndUserId(String paymentKey, Integer userId);
}
