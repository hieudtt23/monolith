package com.danghieu99.monolith.product.repository.jpa.join;

import com.danghieu99.monolith.product.entity.join.ProductShop;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ProductShopRepository extends JpaRepository<ProductShop, Integer> {

    void deleteByProductId(int productId);

    @Transactional
    @Modifying
    @Query("delete ProductCategory pc " +
            "where pc.productId = (select p.id from Product p " +
            "where p.uuid = :productUUID)")
    void deleteByProductUUID(@NotNull UUID productUUID);

    void deleteByShopId(int shopId);
}