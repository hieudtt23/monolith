package com.danghieu99.monolith.ecommerce.product.entity.jpa;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "review_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReviewImage extends BaseEntity {

    @Column(nullable = false)
    private int reviewId;

    @Column(nullable = false)
    private String imageToken;
}
