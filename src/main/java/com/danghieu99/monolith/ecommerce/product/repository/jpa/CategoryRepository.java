package com.danghieu99.monolith.ecommerce.product.repository.jpa;

import com.danghieu99.monolith.ecommerce.product.entity.jpa.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByUuid(UUID uuid);

    Optional<Category> findByName(String name);

    @Query("select c from Category c join Category sc on c.superCategoryId = sc.id where sc.uuid = :superCategoryUUID")
    Page<Category> findBySuperCategoryUUID(UUID superCategoryUUID, Pageable pageable);

    @Query("select sc from Category c join Category sc on c.superCategoryId = sc.id where c.uuid = :subcategoryUUID")
    Page<Category> findBySubCategoryUUID(UUID subCategoryUUID, Pageable pageable);

    @Query("select c from Category c where c.name like concat('%', :name, '%')")
    Page<Category> findByNameContaining(final String name, final Pageable pageable);

    @Query("select c.id from Category c " +
            "where c.name = :name")
    Optional<Integer> findCategoryIdByName(String name);

    @Query("select c from Category c " +
            "join ProductCategory pc on c.id = pc.categoryId " +
            "join Product p on pc.productId = p.id " +
            "where p.uuid = :uuid")
    List<Category> findByProductUUID(UUID productUUID);

    @Query("select c from Category c " +
            "join ProductCategory pc on c.id = pc.categoryId " +
            "where pc.productId = :productId")
    List<Category> findByProductId(int productId);
}
