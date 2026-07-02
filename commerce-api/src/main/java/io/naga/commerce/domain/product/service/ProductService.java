package io.naga.commerce.domain.product.service;

import static io.naga.common.error.ErrorCode.NOT_FOUND_PRODUCT;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.naga.commerce.domain.product.dto.response.ProductResponse;
import io.naga.commerce.domain.product.model.Product;
import io.naga.commerce.domain.product.repository.ProductRepository;
import io.naga.common.error.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> findProducts() {
        return productRepository.findAll()
            .stream()
            .map(ProductResponse::of)
            .toList();
    }

    public ProductResponse findProduct(Integer productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> BusinessException.of(NOT_FOUND_PRODUCT, "productId : " + productId));

        return ProductResponse.of(product);
    }
}
