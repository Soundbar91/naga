package io.naga.commerce.domain.order.model;

import static io.naga.commerce.domain.order.model.OrderStatus.CREATED;
import static io.naga.commerce.domain.order.model.OrderStatus.PAID;
import static io.naga.common.error.ErrorCode.INVALID_ORDER_STATUS;
import static io.naga.common.error.ErrorCode.ORDER_AMOUNT_MISMATCH;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.naga.commerce.domain.user.model.User;
import io.naga.commerce.global.model.BaseEntity;
import io.naga.common.error.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @NotNull
    @Column(name = "order_key", nullable = false, unique = true)
    private String orderKey;

    @NotNull
    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @NotNull
    @Enumerated(STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Builder
    private Order(User user, String orderKey, Integer totalPrice, OrderStatus status) {
        this.user = user;
        this.orderKey = orderKey;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public static Order create(User user, String orderKey, Integer totalPrice) {
        return Order.builder()
            .user(user)
            .orderKey(orderKey)
            .totalPrice(totalPrice)
            .status(CREATED)
            .build();
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }

    public void validatePayment(Integer amount) {
        if (status != CREATED) {
            throw BusinessException.of(INVALID_ORDER_STATUS, "orderKey : " + orderKey + ", status : " + status);
        }
        if (!Objects.equals(totalPrice, amount)) {
            throw BusinessException.of(ORDER_AMOUNT_MISMATCH, "orderKey : " + orderKey);
        }
    }

    public void markAsPaid() {
        if (status != CREATED) {
            throw BusinessException.of(INVALID_ORDER_STATUS, "orderKey : " + orderKey + ", status : " + status);
        }
        status = PAID;
    }
}
