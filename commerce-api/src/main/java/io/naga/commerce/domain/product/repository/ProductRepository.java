package io.naga.commerce.domain.product.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.naga.commerce.domain.product.model.Product;
import jakarta.persistence.LockModeType;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByIdIn(Collection<Integer> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT product
        FROM Product product
        WHERE product.id IN :ids
        """)
    List<Product> findAllByIdInForUpdate(@Param("ids") Collection<Integer> ids);
}
