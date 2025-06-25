package com.danghieu99.monolith.ecommerce.product.entity.jpa;

import com.danghieu99.monolith.common.entity.BaseEntity;
import com.danghieu99.monolith.ecommerce.product.constant.EShopStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "shops")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Shop extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "account_uuid", nullable = false, unique = true, updatable = false)
    private UUID accountUUID;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EShopStatus status;
}
