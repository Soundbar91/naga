package io.naga.commerce.domain.order.service;

import static io.naga.common.error.ErrorCode.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.naga.commerce.domain.order.dto.request.OrderCreateRequest;
import io.naga.commerce.domain.order.dto.response.OrderCreateResponse;
import io.naga.commerce.domain.order.model.Order;
import io.naga.commerce.domain.order.model.OrderItem;
import io.naga.commerce.domain.order.repository.OrderItemRepository;
import io.naga.commerce.domain.order.repository.OrderRepository;
import io.naga.commerce.domain.order.support.OrderKeyGenerator;
import io.naga.commerce.domain.product.model.Product;
import io.naga.commerce.domain.product.repository.ProductRepository;
import io.naga.commerce.domain.user.model.User;
import io.naga.commerce.domain.user.repository.UserRepository;
import io.naga.common.error.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderKeyGenerator orderKeyGenerator;

    @Retryable(
        retryFor = OptimisticLockingFailureException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 50)
    )
    @Transactional
    public OrderCreateResponse createOrder(Integer userId, OrderCreateRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> BusinessException.of(NOT_FOUND_USER, "userId : " + userId));
        Set<Integer> productIds = request.getProductIds();
        Map<Integer, Integer> quantitiesByProductId = request.getQuantitiesByProductId();
        List<Product> products = getProducts(productIds);
        Map<Integer, Product> productsById = products.stream()
            .collect(Collectors.toMap(Product::getId, Function.identity()));
        Integer totalPrice = quantitiesByProductId.entrySet()
            .stream()
            .mapToInt(entry -> productsById.get(entry.getKey()).getPrice() * entry.getValue())
            .sum();

        Order order = orderRepository.save(Order.create(user, orderKeyGenerator.generate(), totalPrice));
        List<OrderItem> orderItems = quantitiesByProductId.entrySet()
            .stream()
            .map(entry -> createOrderItem(order, productsById.get(entry.getKey()), entry.getValue()))
            .toList();
        orderItemRepository.saveAll(orderItems);

        return OrderCreateResponse.of(order);
    }

    private List<Product> getProducts(Set<Integer> productIds) {
        List<Product> products = productRepository.findAllByIdIn(productIds);
        Set<Integer> foundProductIds = products.stream()
            .map(Product::getId)
            .collect(Collectors.toSet());

        if (products.isEmpty()) {
            throw BusinessException.of(NOT_FOUND_PRODUCT, "productIds : " + productIds);
        }
        if (!foundProductIds.equals(productIds)) {
            throw BusinessException.of(PRODUCT_MISMATCH_IN_ORDER, "productIds : " + productIds);
        }

        return products;
    }

    private OrderItem createOrderItem(
        Order order,
        Product product,
        Integer quantity
    ) {
        product.decreaseQuantity(quantity);
        return OrderItem.create(order, product, product.getPrice(), quantity);
    }

    @Recover
    public OrderCreateResponse recover(Integer userId, OrderCreateRequest request) {
        throw BusinessException.of(OUT_OF_STOCK, "userId : " + userId);
    }
}
