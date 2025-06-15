package com.danghieu99.monolith.ecommerce.product.entity.jpa.join;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "product_categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductCategory extends BaseEntity {

    @Column(nullable = false)
    private int productId;

    @Column(nullable = false)
    private int categoryId;
}
