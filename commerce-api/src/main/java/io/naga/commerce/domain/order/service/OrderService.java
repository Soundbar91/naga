package io.naga.commerce.domain.order.service;

import static io.naga.commerce.global.error.ErrorCode.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.naga.commerce.domain.order.dto.request.OrderCreateRequest;
import io.naga.commerce.domain.order.dto.response.OrderCreateResponse;
import io.naga.commerce.domain.order.model.Order;
import io.naga.commerce.domain.order.model.OrderItem;
import io.naga.commerce.domain.order.repository.OrderRepository;
import io.naga.commerce.domain.order.support.OrderKeyGenerator;
import io.naga.commerce.domain.product.model.Product;
import io.naga.commerce.domain.product.repository.ProductRepository;
import io.naga.commerce.domain.user.model.User;
import io.naga.commerce.domain.user.repository.UserRepository;
import io.naga.commerce.global.error.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderKeyGenerator orderKeyGenerator;

    @Transactional
    public OrderCreateResponse createOrder(Integer userId, OrderCreateRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> BusinessException.of(NOT_FOUND_USER, "userId : " + userId));

        Set<Integer> productIds = request.items().stream()
            .map(item -> item.productId())
            .collect(Collectors.toSet());
        Map<Integer, Product> productMap = getProductMap(productIds);
        Integer totalPrice = request.items().stream()
            .mapToInt(item -> productMap.get(item.productId()).getPrice() * item.quantity())
            .sum();

        Order order = Order.create(user, orderKeyGenerator.generate(), totalPrice);
        request.items().forEach(item -> {
            Product product = productMap.get(item.productId());
            product.decreaseQuantity(item.quantity());
            order.addOrderItem(OrderItem.create(product, product.getPrice(), item.quantity()));
        });
        orderRepository.save(order);

        return OrderCreateResponse.of(order);
    }

    private Map<Integer, Product> getProductMap(Set<Integer> productIds) {
        Map<Integer, Product> productMap = productRepository.findAllByIdIn(productIds)
            .stream()
            .collect(Collectors.toMap(Product::getId, product -> product));
        if (productMap.isEmpty()) {
            throw BusinessException.of(NOT_FOUND_PRODUCT, "productIds : " + productIds);
        }
        if (!productMap.keySet().equals(productIds)) {
            throw BusinessException.of(PRODUCT_MISMATCH_IN_ORDER, "productIds : " + productIds);
        }

        return productMap;
    }
}
