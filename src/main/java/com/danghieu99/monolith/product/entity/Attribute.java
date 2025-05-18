package com.danghieu99.monolith.product.entity;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Entity
@Table(name = "attributes",
        uniqueConstraints = {@UniqueConstraint(name = "uq_product_attribute",
                columnNames = "product_id, type, value")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Attribute extends BaseEntity {

    @Column(nullable = false)
    private int productId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String value;
}
