package com.danghieu99.monolith.payment.entity;

import com.danghieu99.monolith.common.entity.BaseEntity;
import com.danghieu99.monolith.payment.constant.EPaymentMethod;
import com.danghieu99.monolith.payment.constant.EPaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "refunds")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Refund extends BaseEntity {

    @Column(nullable = false)
    private int transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EPaymentMethod paymentMethod;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EPaymentStatus status;
}
