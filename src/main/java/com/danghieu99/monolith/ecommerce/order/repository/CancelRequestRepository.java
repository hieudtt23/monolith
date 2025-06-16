package com.danghieu99.monolith.ecommerce.order.repository;

import com.danghieu99.monolith.ecommerce.order.constant.ECancelStatus;
import com.danghieu99.monolith.ecommerce.order.entity.CancelRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CancelRequestRepository extends JpaRepository<CancelRequest, Integer> {

    Optional<CancelRequest> findByUuid(UUID uuid);

    @Query("select cancelRequest from CancelRequest cancelRequest " +
            "join Order order " +
            "on cancelRequest.orderId = order.id " +
            "join Shop shop " +
            "on shop.id = order.shopId " +
            "where shop.accountUUID = :shopAccountUUID")
    Page<CancelRequest> findByShopAccountUUID(String shopAccountUUID, Pageable pageable);

    @Query("update CancelRequest c set " +
            "c.status = :status " +
            "where c.uuid = :uuid")
    @Modifying
    void updateStatusByUuid(UUID uuid, ECancelStatus status);
}
