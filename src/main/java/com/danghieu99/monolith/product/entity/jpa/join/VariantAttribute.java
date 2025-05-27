package com.danghieu99.monolith.product.entity.jpa.join;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Entity
@Table(name = "variant_attributes",
        uniqueConstraints = {@UniqueConstraint(name = "uq_variant_attribute_type",
                columnNames = "id, attribute_type")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VariantAttribute extends BaseEntity {

    @Column(nullable = false)
    private int variantId;

    @Column(nullable = false)
    private int attributeId;

    @Column(nullable = false)
    private String attributeType;
}