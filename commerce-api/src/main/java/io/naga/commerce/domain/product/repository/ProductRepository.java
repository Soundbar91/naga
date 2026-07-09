package io.naga.commerce.domain.product.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.naga.commerce.domain.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByIdIn(Collection<Integer> ids);

    @Modifying
    @Query("""
        UPDATE Product product
        SET product.status = CASE
                WHEN product.quantity - :quantity = 0
                THEN io.naga.commerce.domain.product.model.ProductStatus.SOLD_OUT
                ELSE product.status
            END,
            product.quantity = product.quantity - :quantity,
            product.updatedAt = CURRENT_TIMESTAMP
        WHERE product.id = :productId
            AND product.status = io.naga.commerce.domain.product.model.ProductStatus.SALE
            AND product.quantity >= :quantity
        """)
    int decreaseQuantity(@Param("productId") Integer productId, @Param("quantity") Integer quantity);
}
