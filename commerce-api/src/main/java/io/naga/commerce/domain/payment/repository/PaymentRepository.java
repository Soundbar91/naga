package io.naga.commerce.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.naga.commerce.domain.payment.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
