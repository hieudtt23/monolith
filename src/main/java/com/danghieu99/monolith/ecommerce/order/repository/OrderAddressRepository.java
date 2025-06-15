package com.danghieu99.monolith.ecommerce.order.repository;

import com.danghieu99.monolith.ecommerce.order.entity.OrderAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderAddressRepository extends JpaRepository<OrderAddress, Integer> {
    OrderAddress findByOrderUUID(UUID orderUUID);
}