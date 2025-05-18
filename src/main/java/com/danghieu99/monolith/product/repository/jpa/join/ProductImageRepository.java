package com.danghieu99.monolith.product.repository.jpa.join;

import com.danghieu99.monolith.product.entity.join.ProductImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

    @Modifying
    @Transactional
    @Query("delete from ProductImage pImage " +
            "where pImage.id = (select p.id from Product p where p.uuid = :productUUID)")
    void deleteByProductUUID(UUID productUUID);
}
