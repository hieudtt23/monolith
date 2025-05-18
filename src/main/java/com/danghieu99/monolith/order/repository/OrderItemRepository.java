package com.danghieu99.monolith.order.repository;

import com.danghieu99.monolith.order.entity.OrderItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrderId(Integer orderId);

    @Query("select i from OrderItem i " +
            "join Order o on o.id = i.orderId " +
            "where o.uuid = :orderUUID")
    List<OrderItem> findByOrderUUID(UUID orderUUID);

    @Modifying
    @Transactional
    @Query("delete from OrderItem item " +
            "where item.orderId = " +
            "(select o.id from Order o " +
            "where o.uuid = :orderUUID) ")
    void deleteByOrderUuid(UUID orderUUID);
}