package io.naga.commerce.domain.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.naga.commerce.domain.product.dto.response.ProductResponse;
import io.naga.commerce.domain.product.service.ProductService;
import io.naga.commerce.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findProducts() {
        List<ProductResponse> response = productService.findProducts();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> findProduct(
        @PathVariable Integer productId
    ) {
        ProductResponse response = productService.findProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
