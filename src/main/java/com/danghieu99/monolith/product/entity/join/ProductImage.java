package com.danghieu99.monolith.product.entity.join;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductImage extends BaseEntity {

    @Column(nullable = false)
    private UUID productUUID;

    @Column(nullable = false)
    private UUID imageUUID;
}