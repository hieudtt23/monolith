package com.danghieu99.monolith.ecommerce.product.repository.jpa;

import com.danghieu99.monolith.ecommerce.product.entity.jpa.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer> {

    Optional<Shop> findByName(String name);

    @Query("select s from Shop s where s.name = concat('%', :name, '%')")
    Page<Shop> findByNameContaining(String name, Pageable pageable);

    Optional<Shop> findByUuid(UUID uuid);

    Optional<Shop> findByAccountUUID(String accountUUID);

    @Query("select s.id from Shop s " +
            "where s.accountUUID = :accountUUID")
    Optional<Integer> findShopIdByAccountUUID(String accountUUID);

    @Query("select s from Shop s join ProductShop ps on s.id = ps.shopId join Product p where p.uuid = :productUUID")
    Optional<Shop> findByProductUuid(UUID productUUID);

    void deleteByUuid(UUID uuid);

    void deleteByAccountUUID(String accountUUID);
}