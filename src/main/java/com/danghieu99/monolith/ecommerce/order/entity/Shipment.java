package com.danghieu99.monolith.ecommerce.order.entity;

import com.danghieu99.monolith.common.entity.BaseEntity;
import com.danghieu99.monolith.ecommerce.order.constant.EShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Shipment extends BaseEntity {

    @Column(nullable = false)
    private int orderId;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private String shipmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EShipmentStatus status;
}
