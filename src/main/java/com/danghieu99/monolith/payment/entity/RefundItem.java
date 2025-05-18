package com.danghieu99.monolith.payment.entity;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "refund_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RefundItem extends BaseEntity {

    @Column(nullable = false)
    private int refundId;

    @Column(name = "order_item_uuid", nullable = false)
    private String orderItemUUID;
}