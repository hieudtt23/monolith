package com.danghieu99.monolith.ecommerce.order.entity;

import com.danghieu99.monolith.common.entity.BaseEntity;
import com.danghieu99.monolith.ecommerce.order.constant.EOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Order extends BaseEntity {

    @Column(nullable = false)
    private UUID userAccountUUID;

    @Column(nullable = false)
    private int shopId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EOrderStatus status = EOrderStatus.PENDING;

    @Column(nullable = false)
    private String details;
}