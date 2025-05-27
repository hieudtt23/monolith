package com.danghieu99.monolith.product.repository.jpa.join;

import com.danghieu99.monolith.product.entity.jpa.join.VariantAttribute;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VariantAttributeRepository extends JpaRepository<VariantAttribute, Integer> {

    @Modifying
    @Transactional
    @Query("delete VariantAttribute va " +
            "where va.variantId = (select v.id from Variant v " +
            "where v.uuid = :uuid)")
    void deleteByVariantUUID(@NotBlank UUID variantUUID);

    @Modifying
    @Transactional
    void deleteByAttributeId(@NotBlank int attributeId);

    @Modifying
    @Transactional
    @Query("delete VariantAttribute va " +
            "where va.attributeId = :attributeUUID")
    void deleteByAttributeUUID(@NotBlank UUID attributeUUID);

    @Modifying
    @Transactional
    @Query("delete VariantAttribute va " +
            "where va.variantId = (select v.id from Variant v " +
            "join Product p on v.productId = p.id " +
            "where p.id = :productId)")
    void deleteByProductId(int productId);

    @Modifying
    @Transactional
    @Query("delete VariantAttribute va " +
            "where va.variantId = (select v.id from Variant v " +
            "join Product p on v.productId = p.id " +
            "where p.uuid = :productUUID)")
    void deleteByProductUUID(UUID productUUID);
}
