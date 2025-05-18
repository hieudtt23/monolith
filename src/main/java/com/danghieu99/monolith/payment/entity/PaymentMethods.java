package com.danghieu99.monolith.payment.entity;

import com.danghieu99.monolith.common.entity.BaseEntity;
import com.danghieu99.monolith.payment.constant.EPaymentMethod;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PaymentMethods extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EPaymentMethod paymentMethod;

    @Column(nullable = false)
    private String externalPaymentId;

    @Column(nullable = false)
    private String transactionToken;
}