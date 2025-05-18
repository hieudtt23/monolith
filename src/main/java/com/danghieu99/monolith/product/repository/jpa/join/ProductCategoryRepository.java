package com.danghieu99.monolith.product.repository.jpa.join;

import com.danghieu99.monolith.product.entity.join.ProductCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    Set<ProductCategory> findByProductId(Integer productId);

    @Query("select pc from ProductCategory pc join Product p on pc.productId = p.id where p.uuid = :uuid")
    Set<ProductCategory> findByProductUuid(UUID productUUID);

    void deleteByCategoryId(int categoryId);

    void deleteByProductId(int productId);

    @Transactional
    @Modifying
    @Query("delete ProductCategory pc " +
            "where pc.productId = (select p.id from Product p " +
            "where p.uuid = :productUUID)")
    void deleteByProductUUID(UUID productUUID);
}
