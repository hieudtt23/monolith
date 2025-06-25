package com.danghieu99.monolith.ecommerce.order.repository;

import com.danghieu99.monolith.ecommerce.order.constant.EOrderStatus;
import com.danghieu99.monolith.ecommerce.order.entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserAccountUUID(UUID uuid);

    Optional<Order> findByUuid(UUID uuid);

    @Modifying
    @Transactional
    @Query("update Order o " +
            "set o.status = :status " +
            "where o.uuid = :uuid")
    int updateOrderStatusByUUID(UUID uuid, EOrderStatus status);

    @Modifying
    @Transactional
    @Query("update Order o " +
            "set o.status  = :status," +
            "o.details = :details " +
            "where o.uuid = :uuid")
    int updateOrderStatusAndDetails(UUID uuid, EOrderStatus status, String details);

    Page<Order> findByShopId(int shopId, Pageable pageable);

    @Query("select o from Order o " +
            "join Shop s on o.shopId = s.id " +
            "where s.uuid = :shopUUID")
    Page<Order> findByShopUUID(UUID shopUUID, Pageable pageable);

    Page<Order> findByShopIdAndStatus(int shopId, EOrderStatus status, Pageable pageable);

    @Query("select o from Order o " +
            "join Shop s on o.shopId = s.id " +
            "where s.uuid = :shopUUID " +
            "and o.status = :status")
    Page<Order> findByShopUUIDAndStatus(UUID shopUUID, EOrderStatus status, Pageable pageable);

    void deleteByUuid(UUID uuid);
}
