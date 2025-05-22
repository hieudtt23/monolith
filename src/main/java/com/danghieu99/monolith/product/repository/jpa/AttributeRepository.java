package com.danghieu99.monolith.product.repository.jpa;

import com.danghieu99.monolith.product.entity.Attribute;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Integer> {

    @Query("select a from Attribute a " +
            "join Product p on a.productId = p.id " +
            "where p.uuid = :uuid")
    List<Attribute> findByProductUUID(UUID productUUID);

    @Query("select a from Attribute a " +
            "join Product p on p.id = a.productId " +
            "where p.uuid = :productUUID " +
            "and a.type like concat('%', lower(:type), '%')" +
            "and a.value like concat('%', lower(:value), '%')")
    Optional<Attribute> findByProductUUIDAttributeTypeValueContainsIgnoreCase(UUID productUUID, String type, String value);

    @Transactional
    @Modifying
    @Query("delete from Attribute a " +
            "where a.id = (select a.id from Attribute a " +
            "join Product p on p.id = a.productId " +
            "where p.uuid = :productUUID " +
            "and a.type like concat('%', lower(:type), '%'))" +
            "and a.value like concat('%', lower(:value), '%')")
    void deleteByProductUUIDAttributeTypeValueContainsIgnoreCase(UUID productUUID, String type, String value);

    void deleteByUuid(UUID uuid);

    @Transactional
    @Modifying
    @Query("delete Attribute a " +
            "where a.productId = (select p.id from Product p " +
            "where p.uuid = :productUUID)" +
            "and a.type like concat('%', lower(:type), '%')" +
            "and a.value like concat('%', lower(:value), '%')")
    void deleteByTypeAndValueContainsIgnoreCase(UUID productUUID, String type, String value);

    List<Attribute> uuid(UUID uuid);
}