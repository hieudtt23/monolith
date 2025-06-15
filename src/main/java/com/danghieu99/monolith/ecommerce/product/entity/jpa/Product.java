package com.danghieu99.monolith.ecommerce.product.entity.jpa;

import com.danghieu99.monolith.common.entity.BaseEntity;
import com.danghieu99.monolith.ecommerce.product.constant.EProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Product extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal basePrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EProductStatus status;
}