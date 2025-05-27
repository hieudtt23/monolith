package com.danghieu99.monolith.product.repository.jpa;

import com.danghieu99.monolith.product.entity.jpa.Variant;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Integer> {

    Optional<Variant> findByUuid(UUID uuid);

    @Query("select v from Variant v " +
            "where v.productId = :productId")
    Set<Variant> findByProductId(@NotNull int productId);

    @Query("select v from Variant v " +
            "where v.productId = :productId")
    Page<Variant> findByProductId(@NotNull int productId, @NotNull Pageable pageable);

    @Query("select v from Variant v " +
            "join Product p on v.productId = p.id " +
            "where p.uuid = :productUUID")
    Page<Variant> findByProductUuid(UUID productUUID, Pageable pageable);

    @Query("select v from Variant v " +
            "join Product p on v.productId = p.id " +
            "where p.uuid = :productUUID")
    Set<Variant> findByProductUuid(UUID productUUID);

    void deleteByUuid(UUID uuid);

    void deleteByProductId(int productId);

    @Transactional
    @Modifying
    @Query("delete Variant v " +
            "where v.productId = (select p.id from Product p " +
            "where p.uuid = :productUUID)")
    void deleteByProductUUID(UUID productUUID);

}