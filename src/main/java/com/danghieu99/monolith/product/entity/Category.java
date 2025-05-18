package com.danghieu99.monolith.product.entity;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categories",
        uniqueConstraints = {@UniqueConstraint(name = "uq_super_category",
                columnNames = "id, super_category_id")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Category extends BaseEntity {

    @Column(nullable = false)
    private int superCategoryId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;
}