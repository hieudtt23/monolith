package com.danghieu99.monolith.ecommerce.product.entity.jpa;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "global_attributes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GlobalAttribute extends BaseEntity {

    @Column(nullable = false)
    private int globalAttributeTypeId;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private String description;
}