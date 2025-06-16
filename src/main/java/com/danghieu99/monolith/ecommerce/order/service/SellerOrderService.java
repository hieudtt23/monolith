package com.danghieu99.monolith.ecommerce.order.service;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.ecommerce.order.constant.ECancelStatus;
import com.danghieu99.monolith.ecommerce.order.constant.EOrderStatus;
import com.danghieu99.monolith.ecommerce.order.dto.request.ProcessCancelRequestRequest;
import com.danghieu99.monolith.ecommerce.order.dto.response.CancelOrderRequestDetailsResponse;
import com.danghieu99.monolith.ecommerce.order.dto.response.OrderDetailsResponse;
import com.danghieu99.monolith.ecommerce.order.entity.CancelRequest;
import com.danghieu99.monolith.ecommerce.order.mapper.OrderMapper;
import com.danghieu99.monolith.ecommerce.order.repository.CancelRequestRepository;
import com.danghieu99.monolith.ecommerce.order.repository.OrderRepository;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
// TODO: write to  transactional outbox table -> debezium write to kafka
public class SellerOrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CancelRequestRepository cancelRequestRepository;

    public Page<OrderDetailsResponse> getAllOrdersByCurrentShop(@NotNull UserDetailsImpl userDetails, @NotNull Pageable pageable) {
        var orders = orderRepository.findByShopUUID(UUID.fromString(userDetails.getUuid()), pageable);
        return orders.map(orderMapper::toOrderDetailsResponse);
    }

    public Page<OrderDetailsResponse> getAllOrdersByCurrentShopAndStatus(@NotNull UserDetailsImpl userDetails,
                                                                         @NotNull EOrderStatus status,
                                                                         @NotNull Pageable pageable) {
        var orders = orderRepository.findByShopUUIDAndStatus(UUID.fromString(userDetails.getUuid()), status, pageable);
        return orders.map(orderMapper::toOrderDetailsResponse);
    }

    public Page<CancelOrderRequestDetailsResponse> getCancelOrderRequestsByCurrentShop(@NotNull UserDetailsImpl userDetails,
                                                                                       @NotNull Pageable pageable) {

        var cancels = cancelRequestRepository.findByShopAccountUUID(userDetails.getUuid(), pageable);
        return cancels.map(orderMapper::toCancelOrderRequestDetails);
    }

    @Async
    @Transactional
    public void processCancelRequest(@NotNull ProcessCancelRequestRequest request) {
        CancelRequest cancelRequest = cancelRequestRepository.findByUuid(UUID.fromString(request.getCancelRequestUUID()))
                .orElseThrow(() -> new ResourceNotFoundException("CancelRequest", "uuid", request.getCancelRequestUUID()));
        if (request.isAccept()) {
            cancelRequestRepository.updateStatusByUuid(cancelRequest.getUuid(), ECancelStatus.ACCEPTED);
        } else {
            cancelRequestRepository.updateStatusByUuid(cancelRequest.getUuid(), ECancelStatus.DENIED);
        }
    }
}