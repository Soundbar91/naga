package io.naga.commerce.domain.product.dto.response;

import io.naga.commerce.domain.product.model.Product;
import io.naga.commerce.domain.product.model.ProductStatus;

public record ProductResponse(
    Integer id,
    String name,
    Integer price,
    Integer quantity,
    ProductStatus status
) {

    public static ProductResponse of(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getQuantity(),
            product.getStatus()
        );
    }
}
