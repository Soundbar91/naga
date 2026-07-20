package io.naga.commerce.domain.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.naga.commerce.domain.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findByOrderKeyAndUserId(String orderKey, Integer userId);
}
