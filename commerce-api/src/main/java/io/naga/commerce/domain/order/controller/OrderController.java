package io.naga.commerce.domain.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.naga.commerce.domain.order.dto.request.OrderCreateRequest;
import io.naga.commerce.domain.order.dto.response.OrderCreateResponse;
import io.naga.commerce.domain.order.service.OrderService;
import io.naga.commerce.global.auth.UserId;
import io.naga.commerce.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderCreateResponse>> createOrder(
        @UserId Integer userId,
        @Valid @RequestBody OrderCreateRequest request
    ) {
        OrderCreateResponse response = orderService.createOrder(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
