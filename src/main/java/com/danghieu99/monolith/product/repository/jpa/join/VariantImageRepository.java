package com.danghieu99.monolith.product.repository.jpa.join;

import com.danghieu99.monolith.product.entity.jpa.join.VariantImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface VariantImageRepository extends JpaRepository<VariantImage, Integer> {

    @Modifying
    @Transactional
    @Query("delete VariantImage vi " +
            "where vi.id  = (select p.id from Product p where p.uuid = :productUUID)")
    void deleteByProductUUID(UUID productUUID);

    @Modifying
    @Transactional
    @Query("delete VariantImage vi " +
            "where vi.id = (select v.id from Variant v where v.uuid = :variantUUID)")
    void deleteByVariantUUID(UUID variantUUID);
}
