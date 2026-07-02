package io.naga.commerce.domain.product.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    SALE("판매"),
    SOLD_OUT("품절"),
    ;

    private final String description;
}
