package com.danghieu99.monolith.product.entity.jpa;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "global_product_attributes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GlobalAttributeType extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;
}
