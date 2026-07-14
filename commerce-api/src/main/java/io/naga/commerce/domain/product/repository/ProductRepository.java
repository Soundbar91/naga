package io.naga.commerce.domain.product.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.naga.commerce.domain.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByIdIn(Collection<Integer> ids);
}
