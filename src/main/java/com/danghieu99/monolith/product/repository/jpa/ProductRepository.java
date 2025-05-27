package com.danghieu99.monolith.product.repository.jpa;

import com.danghieu99.monolith.product.entity.jpa.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByUuid(UUID uuid);

    Optional<Product> findByName(String name);

    @Query("select p from Product p where p.name like %:name%")
    List<Product> findByNameContaining(String name);

    @Query("select p from Product p join ProductCategory pc on p.id = pc.productId where pc.productId = :productId")
    List<Product> findBySameCategoryProductId(int productId);

    @Query("select p from Product p join ProductCategory pc on p.id = pc.productId where pc.categoryId = :categoryId")
    List<Product> findByCategoryId(int categoryId);

    @Query("select p from Product p join ProductCategory pc on p.id = pc.productId where pc.categoryId = :categoryId")
    Page<Product> findByCategoryId(int categoryId, Pageable pageable);

    @Query("select p from Product p " +
            "join ProductCategory pc on p.id = pc.productId " +
            "join Category c on pc.categoryId = c.id " +
            "where pc.categoryId = :categoryId " +
            "or c.superCategoryId = :categoryId")
    Page<Product> findByCategoryIdAndSuperCategoryId(int categoryId, Pageable pageable);

    @Query("select p from Product p " +
            "join ProductCategory pc on p.id = pc.productId " +
            "join Category c on pc.categoryId = c.superCategoryId " +
            "where pc.categoryId = :categoryId " +
            "or c.id = :categoryId")
    Page<Product> findByCategoryIdAndSubCategoryId(int categoryId, Pageable pageable);

    @Query("select p from Product p " +
            "join ProductCategory pc on p.id = pc.productId " +
            "join Category c on pc.categoryId = c.id " +
            "join Category c1 on pc.categoryId = c.superCategoryId " +
            "where pc.categoryId = :categoryId " +
            "or c.superCategoryId = :categoryId " +
            "or c1.id = :categoryId")
    Page<Product> findByCategoryAndSuperCategoryAndSubCategoryId(int categoryId, Pageable pageable);

    @Query("select p from Product p " +
            "join ProductCategory pc on p.id = pc.productId " +
            "where pc.categoryId in :categoryIds")
    List<Product> findByCategoryIdContain(Collection<Integer> categoryIds);

    @Query("select p from Product p " +
            "join ProductCategory pc on p.id = pc.productId " +
            "where pc.categoryId in :categoryIds")
    Page<Product> findByCategoryIdContain(Collection<Integer> categoryIds, Pageable pageable);

    @Query("select p from Product p " +
            "join ProductCategory pc on pc.productId = p.id " +
            "join Category c on c.id = pc.categoryId " +
            "where c.uuid = :uuid")
    Page<Product> findByCategoryUUID(UUID uuid, Pageable pageable);

    @Query("select p from Product p " +
            "join ProductCategory pc on pc.productId = p.id " +
            "join Category c on c.id = pc.categoryId " +
            "where c.uuid in :uuids")
    Page<Product> findByCategoryUUIDsAny(List<UUID> uuids, Pageable pageable);

    @Query("select p from Product p " +
            "join ProductShop ps on p.id = ps.productId " +
            "where ps.shopId = :shopId")
    List<Product> findByShopId(int id);

    @Query("select p from Product p " +
            "join ProductShop ps on p.id = ps.productId " +
            "where ps.shopId = :shopId")
    Page<Product> findByShopId(int shopId, Pageable pageable);

    @Query("select p from Product p " +
            "join ProductShop ps on ps.productId = p.id " +
            "join Shop s on s.id = ps.shopId " +
            "where s.uuid = :uuid")
    List<Product> findByShopUuid(UUID uuid);

    @Query("select p from Product p " +
            "join ProductShop ps on ps.productId = p.id " +
            "join Shop s on s.id = ps.shopId " +
            "where s.uuid = :uuid")
    Page<Product> findByShopUUID(UUID uuid, Pageable pageable);

    @Query("select p from Product p " +
            "where p.name like %:name%")
    Page<Product> findByNameContaining(String name, Pageable pageable);

    @Transactional
    void deleteByUuid(UUID uuid);

    @Transactional
    @Modifying
    @Query("delete Product p " +
            "where p.id = " +
            "(select ps.id from ProductShop ps " +
            "where ps.shopId = :shopId)")
    void deleteByShopId(int shopId);

    @Transactional
    @Modifying
    @Query("delete Product p " +
            "where p.id = " +
            "(select ps.id from ProductShop ps " +
            "join Shop s on s.id = ps.shopId " +
            "where s.uuid = :shopUUID)")
    void deleteByShopUUID(UUID shopUUID);
}