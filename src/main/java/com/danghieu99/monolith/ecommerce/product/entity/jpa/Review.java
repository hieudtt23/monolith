package com.danghieu99.monolith.ecommerce.product.entity.jpa;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Review extends BaseEntity {

    @Column(nullable = false)
    private int productId;

    @Column(nullable = false)
    private int variantId;

    @Column(nullable = false)
    private int rating;

    //clean up html with jsoup, only keep line break tags <br>
    @Column(nullable = false)
    private String content;
}
