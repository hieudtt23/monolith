package com.danghieu99.monolith.product.entity.join;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Entity
@Table(name = "product_global_attributes",
        uniqueConstraints = {@UniqueConstraint(name = "uq_product_global_attribute",
                columnNames = "product_id, global_attribute_id")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductGlobalAttribute extends BaseEntity {

    @Column(nullable = false)
    private int productId;

    @Column(nullable = false)
    private int globalAttributeId;
}
