package io.naga.commerce.domain.order.service;

import static io.naga.commerce.domain.order.model.OrderStatus.CREATED;
import static io.naga.common.error.ErrorCode.NOT_FOUND_PRODUCT;
import static io.naga.common.error.ErrorCode.NOT_FOUND_USER;
import static io.naga.common.error.ErrorCode.PRODUCT_MISMATCH_IN_ORDER;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.naga.commerce.domain.order.dto.request.OrderCreateRequest;
import io.naga.commerce.domain.order.dto.response.OrderCreateResponse;
import io.naga.commerce.domain.order.model.Order;
import io.naga.commerce.domain.order.model.OrderItem;
import io.naga.commerce.domain.order.repository.OrderItemRepository;
import io.naga.commerce.domain.order.repository.OrderRepository;
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

    @Transactional
    public OrderCreateResponse createOrder(Integer userId, OrderCreateRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> BusinessException.of(NOT_FOUND_USER, "userId : " + userId));
        Set<Integer> productIds = request.getProductIds();
        Map<Integer, Integer> quantitiesByProductId = request.getQuantitiesByProductId();
        Map<Integer, Product> productsById = getProductsById(productIds);

        Order order = orderRepository.save(Order.create(user, CREATED));
        List<OrderItem> orderItems = quantitiesByProductId.entrySet()
            .stream()
            .map(entry -> createOrderItem(order, productsById, entry.getKey(), entry.getValue()))
            .toList();
        orderItemRepository.saveAll(orderItems);

        return OrderCreateResponse.of(order);
    }

    private Map<Integer, Product> getProductsById(Set<Integer> productIds) {
        Map<Integer, Product> productsById = productRepository.findAllByIdIn(productIds)
            .stream()
            .collect(Collectors.toMap(Product::getId, Function.identity()));

        if (productsById.isEmpty()) {
            throw BusinessException.of(NOT_FOUND_PRODUCT, "productIds : " + productIds);
        }
        if (!productsById.keySet().equals(productIds)) {
            throw BusinessException.of(PRODUCT_MISMATCH_IN_ORDER, "productIds : " + productIds);
        }

        return productsById;
    }

    private OrderItem createOrderItem(
        Order order,
        Map<Integer, Product> productsById,
        Integer productId,
        Integer quantity
    ) {
        Product product = productsById.get(productId);
        product.decreaseQuantity(quantity);
        return OrderItem.create(order, product, product.getPrice(), quantity);
    }
}
