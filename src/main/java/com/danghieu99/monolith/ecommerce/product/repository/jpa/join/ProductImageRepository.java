package com.danghieu99.monolith.ecommerce.product.repository.jpa.join;

import com.danghieu99.monolith.ecommerce.product.entity.jpa.join.ProductImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

    @Modifying
    @Transactional
    @Query("delete from ProductImage pImage " +
            "where pImage.id = (select p.id from Product p where p.uuid = :productUUID)")
    void deleteByProductUUID(UUID productUUID);

    @Modifying
    @Transactional
    @Query("update ProductImage pi " +
            "set pi.role = :role " +
            "where pi.imageToken = " +
            "(select i.token from Image i " +
            "where i.token = :token)")
    void updateRoleByImageToken(String token, String role);

    @Modifying
    @Transactional
    @Query("delete from ProductImage pi " +
            "where pi.imageToken = " +
            "(select i.token from Image i " +
            "where i.token = :token)")
    void deleteByImageToken(String token);

    Optional<ProductImage> findByImageToken(String imageToken);

    boolean existsByImageToken(String imageToken);

    @Modifying
    @Transactional
    @Query("select pi from ProductImage pi " +
            "where pi.productId = " +
            "(select p.id from Product p " +
            "where p.uuid = :productUUID)")
     Collection<ProductImage> findByProductUUID(UUID productUUID);
}
