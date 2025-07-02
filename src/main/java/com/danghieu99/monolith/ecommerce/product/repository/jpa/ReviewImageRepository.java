package com.danghieu99.monolith.ecommerce.product.repository.jpa;

import com.danghieu99.monolith.ecommerce.product.entity.jpa.ReviewImage;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Integer> {

    Collection<@NotBlank ReviewImage> findByReviewId(int reviewId);
}
