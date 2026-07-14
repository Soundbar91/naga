package io.naga.commerce.domain.product.model;

import static io.naga.commerce.domain.product.model.ProductStatus.SALE;
import static io.naga.commerce.domain.product.model.ProductStatus.SOLD_OUT;
import static lombok.AccessLevel.PROTECTED;

import io.naga.commerce.global.model.BaseEntity;
import io.naga.common.error.BusinessException;
import io.naga.common.error.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @Builder
    private Product(String name, Integer price, Integer quantity, ProductStatus status) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    public static Product create(String name, Integer price, Integer quantity, ProductStatus status) {
        return Product.builder()
            .name(name)
            .price(price)
            .quantity(quantity)
            .status(status)
            .build();
    }

    public void decreaseQuantity(Integer orderQuantity) {
        if (status != SALE) {
            throw BusinessException.of(ErrorCode.NOT_SALE_PRODUCT, "productId : " + id);
        }
        if (quantity < orderQuantity) {
            throw BusinessException.of(ErrorCode.OUT_OF_STOCK, "productId : " + id);
        }

        quantity -= orderQuantity;
        if (quantity == 0) {
            status = SOLD_OUT;
        }
    }
}
