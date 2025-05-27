package com.danghieu99.monolith.product.repository.jpa;

import com.danghieu99.monolith.product.constant.EImageRole;
import com.danghieu99.monolith.product.entity.jpa.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query("select i from Image i " +
            "join ProductImage pi on pi.imageToken = i.token " +
            "where pi.productUUID = :productUUID")
    List<Image> findByProductUUID(UUID productUUID);

    @Query("select i from Image i " +
            "join ProductImage pi on pi.imageToken = i.token " +
            "where pi.productUUID = :productUUID and pi.role = :role")
    Optional<Image> findByProductUUIDAndRole(UUID productUUID, EImageRole role);

    void deleteByToken(String publicId);

    @Transactional
    @Modifying
    @Query("delete from Image i " +
            "where i.token = " +
            "(select pi.imageToken from ProductImage pi " +
            "where pi.productUUID = :uuid)")
    void deleteByProductUUID(UUID uuid);
}