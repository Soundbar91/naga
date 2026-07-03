package io.naga.commerce.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.naga.commerce.domain.order.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
