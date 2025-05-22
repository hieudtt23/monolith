package com.danghieu99.monolith.product.repository.jpa;

import com.danghieu99.monolith.product.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query("select i from Image i " +
            "join ProductImage pi on pi.imageUUID = i.uuid " +
            "where pi.productUUID = :productUUID")
    List<Image> findByProductUUID(UUID productUUID);
}