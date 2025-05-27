package com.danghieu99.monolith.product.entity.jpa.join;

import com.danghieu99.monolith.common.entity.BaseEntity;
import com.danghieu99.monolith.product.constant.EImageRole;
import jakarta.persistence.*;
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
    private String imageToken;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EImageRole role = EImageRole.GALLERY;
}