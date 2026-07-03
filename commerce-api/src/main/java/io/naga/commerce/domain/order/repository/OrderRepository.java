package io.naga.commerce.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.naga.commerce.domain.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
