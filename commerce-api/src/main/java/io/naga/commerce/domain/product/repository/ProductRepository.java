package io.naga.commerce.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.naga.commerce.domain.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
