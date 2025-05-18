package com.danghieu99.monolith.order.service;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.order.constant.ECancelStatus;
import com.danghieu99.monolith.order.constant.EOrderStatus;
import com.danghieu99.monolith.order.dto.kafka.CancelOrderKafkaMessage;
import com.danghieu99.monolith.order.dto.request.ProcessCancelRequestRequest;
import com.danghieu99.monolith.order.dto.response.CancelOrderRequestDetailsResponse;
import com.danghieu99.monolith.order.dto.response.OrderDetailsResponse;
import com.danghieu99.monolith.order.entity.CancelRequest;
import com.danghieu99.monolith.order.kafka.producer.CancelOrderKafkaProducer;
import com.danghieu99.monolith.order.mapper.OrderMapper;
import com.danghieu99.monolith.order.repository.CancelRequestRepository;
import com.danghieu99.monolith.order.repository.OrderRepository;
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
    private final CancelOrderKafkaProducer cancelOrderKafkaProducer;

    public Page<OrderDetailsResponse> getAllOrdersByCurrentShop(@NotNull UserDetailsImpl userDetails,
                                                                @NotNull Pageable pageable) {
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
            CancelOrderKafkaMessage message = CancelOrderKafkaMessage.builder()
                    .orderUUID(cancelRequest.getOrderUUID().toString())
                    .reason(cancelRequest.getReason())
                    .build();
            cancelOrderKafkaProducer.send(message);
            cancelRequestRepository.updateStatusByUuid(UUID.fromString(request.getCancelRequestUUID()), ECancelStatus.ACCEPTED);
        } else {
            cancelRequestRepository.updateStatusByUuid(UUID.fromString(request.getCancelRequestUUID()), ECancelStatus.DENIED);
        }
    }
}