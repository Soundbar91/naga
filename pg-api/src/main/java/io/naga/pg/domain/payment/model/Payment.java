package io.naga.pg.domain.payment.model;

import static io.naga.common.error.ErrorCode.PAYMENT_ALREADY_PROCESSED;
import static io.naga.common.error.ErrorCode.PAYMENT_INFO_MISMATCH;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import java.util.Objects;

import io.naga.common.error.BusinessException;
import io.naga.pg.domain.user.model.User;
import io.naga.pg.global.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_id", length = 255)
    private String orderId;

    @Column(name = "amount")
    private Integer amount;

    @Enumerated(STRING)
    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "payment_key", length = 255, unique = true)
    private String paymentKey;

    @Column(name = "requested_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime requestedAt;

    @Column(name = "approved_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime approvedAt;

    @Column(name = "canceled_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime canceledAt;

    @Column(name = "failed_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime failedAt;

    @Builder
    private Payment(
        User user,
        String orderId,
        Integer amount,
        PaymentStatus status,
        String paymentKey,
        LocalDateTime requestedAt,
        LocalDateTime approvedAt,
        LocalDateTime canceledAt,
        LocalDateTime failedAt
    ) {
        this.user = user;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.paymentKey = paymentKey;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.canceledAt = canceledAt;
        this.failedAt = failedAt;
    }

    public static Payment createRequested(
        User user,
        String orderId,
        Integer amount,
        String paymentKey
    ) {
        return Payment.builder()
            .user(user)
            .orderId(orderId)
            .amount(amount)
            .status(PaymentStatus.REQUESTED)
            .paymentKey(paymentKey)
            .requestedAt(LocalDateTime.now())
            .build();
    }

    public void approve(String orderId, Integer amount) {
        if (status != PaymentStatus.REQUESTED) {
            throw BusinessException.of(
                PAYMENT_ALREADY_PROCESSED,
                "paymentKey : " + paymentKey + ", status : " + status
            );
        }
        if (!Objects.equals(this.orderId, orderId) || !Objects.equals(this.amount, amount)) {
            throw BusinessException.of(PAYMENT_INFO_MISMATCH, "paymentKey : " + paymentKey);
        }

        status = PaymentStatus.APPROVED;
        approvedAt = LocalDateTime.now();
    }
}
