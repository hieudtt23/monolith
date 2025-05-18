package com.danghieu99.monolith.order.service;

import com.danghieu99.monolith.order.dto.kafka.CancelOrderKafkaMessage;
import com.danghieu99.monolith.order.dto.kafka.RequestCancelOrderKafkaMessage;
import com.danghieu99.monolith.order.dto.kafka.UpdateOrderAddressKafkaMessage;
import com.danghieu99.monolith.order.dto.kafka.PlaceOrderKafkaMessage;
import com.danghieu99.monolith.order.entity.CancelRequest;
import com.danghieu99.monolith.order.entity.Order;
import com.danghieu99.monolith.order.entity.OrderAddress;
import com.danghieu99.monolith.order.entity.OrderItem;
import com.danghieu99.monolith.order.repository.CancelRequestRepository;
import com.danghieu99.monolith.order.repository.OrderAddressRepository;
import com.danghieu99.monolith.order.repository.OrderItemRepository;
import com.danghieu99.monolith.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderAddressRepository orderAddressRepository;
    private final CancelRequestRepository cancelRequestRepository;

    @Async
    @Transactional
    public void placeOrder(@NotNull PlaceOrderKafkaMessage request) {
        List<OrderItem> orderItems = new ArrayList<>();
        Order newOrder = Order.builder()
                .shopUUID(UUID.fromString(request.getShopUUID()))
                .userAccountUUID(UUID.fromString(request.getAccountUUID()))
                .build();
        Order savedOrder = orderRepository.save(newOrder);

        request.getItems().forEach(requestItem -> {
            OrderItem newOrderItem = OrderItem.builder()
                    .orderId(savedOrder.getId())
                    .variantUUID(UUID.fromString(requestItem.getVariantUUID()))
                    .quantity(requestItem.getQuantity())
                    .build();
            orderItems.add(newOrderItem);
        });
        orderItemRepository.saveAll(orderItems);
    }

    @Async
    @Transactional
    public void updateAddress(@NotNull UpdateOrderAddressKafkaMessage request) {
        OrderAddress current = orderAddressRepository.findByOrderUUID(UUID.fromString(request.getOrderUUID()));
        current.setName(request.getNewAddress().getName());
        current.setPhone(request.getNewAddress().getPhone());
        current.setLine1(request.getNewAddress().getLine1());
        current.setLine2(request.getNewAddress().getLine2());
        current.setCity(request.getNewAddress().getCity());
        current.setCountry(request.getNewAddress().getCountry());
        current.setPostalCode(request.getNewAddress().getPostalCode());
        orderAddressRepository.save(current);
    }

    @Async
    @Transactional
    public void placeCancelRequest(@NotNull RequestCancelOrderKafkaMessage request) {
        CancelRequest cancelRequest = CancelRequest.builder()
                .userAccountUUID(UUID.fromString(request.getAccountUUID()))
                .orderUUID(UUID.fromString(request.getOrderUUID()))
                .reason(request.getReason())
                .build();
        cancelRequestRepository.save(cancelRequest);
    }

    @Async
    @Transactional
    public void cancelOrder(@NotNull CancelOrderKafkaMessage request) {
        UUID uuid = UUID.fromString(request.getOrderUUID());
        orderRepository.deleteByUuid(uuid);
        orderItemRepository.deleteByOrderUuid(uuid);
    }
}
