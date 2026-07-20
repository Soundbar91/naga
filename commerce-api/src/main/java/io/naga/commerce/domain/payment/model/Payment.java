package io.naga.commerce.domain.payment.model;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import io.naga.commerce.domain.order.model.Order;
import io.naga.commerce.global.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @NotNull
    @Column(name = "payment_key", nullable = false, unique = true)
    private String paymentKey;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull
    @Column(name = "approved_at", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime approvedAt;

    @Builder
    private Payment(
        Order order,
        String paymentKey,
        Integer amount,
        String status,
        LocalDateTime approvedAt
    ) {
        this.order = order;
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.status = status;
        this.approvedAt = approvedAt;
    }

    public static Payment createApproved(
        Order order,
        String paymentKey,
        Integer amount,
        String status,
        LocalDateTime approvedAt
    ) {
        return Payment.builder()
            .order(order)
            .paymentKey(paymentKey)
            .amount(amount)
            .status(status)
            .approvedAt(approvedAt)
            .build();
    }
}
