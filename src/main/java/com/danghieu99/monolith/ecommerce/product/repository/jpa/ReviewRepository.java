package com.danghieu99.monolith.ecommerce.product.repository.jpa;

import com.danghieu99.monolith.ecommerce.product.entity.jpa.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Collection<Review> findByProductId(int productId);

    Page<Review> findByProductId(int productId, Pageable pageable);
}
