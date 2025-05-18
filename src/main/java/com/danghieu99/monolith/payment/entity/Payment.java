package com.danghieu99.monolith.payment.entity;

import com.danghieu99.monolith.common.entity.BaseEntity;
import com.danghieu99.monolith.payment.constant.EPaymentMethod;
import com.danghieu99.monolith.payment.constant.EPaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Payment extends BaseEntity {

    @Column(nullable = false)
    private String userAccountUUID;

    @Column(nullable = false)
    private String shopAccountUUID;

    @Column(nullable = false)
    private String orderUUID;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EPaymentStatus status;

    @Column(nullable = false)
    private int paymentMethodId;

    @Column
    private Instant closedAt;
}