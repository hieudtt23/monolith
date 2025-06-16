package com.danghieu99.monolith.ecommerce.order.repository;

import com.danghieu99.monolith.ecommerce.order.entity.OrderAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrderAddressRepository extends JpaRepository<OrderAddress, Integer> {

    @Query("select oa from OrderAddress oa " +
            "join Order o on oa.orderId = o.id " +
            "where o.uuid = :orderUUID")
    OrderAddress findByOrderUUID(UUID orderUUID);
}