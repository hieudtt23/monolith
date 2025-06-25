package com.danghieu99.monolith.ecommerce.order.entity;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "order_items",
        uniqueConstraints = {@UniqueConstraint(columnNames = "order_id, variant_uuid")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderItem extends BaseEntity {

    @Column(nullable = false)
    private int orderId;

    @Column(nullable = false)
    private int productId;

    @Column(nullable = false)
    private int variantId;

    @Column(nullable = false)
    private int quantity;
}