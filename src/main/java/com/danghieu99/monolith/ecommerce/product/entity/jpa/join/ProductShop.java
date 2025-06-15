package com.danghieu99.monolith.ecommerce.product.entity.jpa.join;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Entity
@Table(name = "product_shops",
        uniqueConstraints = {@UniqueConstraint(name = "uq_product_shop",
                columnNames = "product_id, shop_id")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductShop extends BaseEntity {

    @Column(nullable = false)
    private int productId;

    @Column(nullable = false)
    private int shopId;
}
