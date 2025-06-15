package com.danghieu99.monolith.ecommerce.order.service;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.ecommerce.order.dto.kafka.CancelOrderKafkaMessage;
import com.danghieu99.monolith.ecommerce.order.dto.kafka.RequestCancelOrderKafkaMessage;
import com.danghieu99.monolith.ecommerce.order.dto.kafka.UpdateOrderAddressKafkaMessage;
import com.danghieu99.monolith.ecommerce.order.dto.kafka.PlaceOrderKafkaMessage;
import com.danghieu99.monolith.ecommerce.order.entity.CancelRequest;
import com.danghieu99.monolith.ecommerce.order.entity.Order;
import com.danghieu99.monolith.ecommerce.order.entity.OrderAddress;
import com.danghieu99.monolith.ecommerce.order.entity.OrderItem;
import com.danghieu99.monolith.ecommerce.order.repository.CancelRequestRepository;
import com.danghieu99.monolith.ecommerce.order.repository.OrderAddressRepository;
import com.danghieu99.monolith.ecommerce.order.repository.OrderItemRepository;
import com.danghieu99.monolith.ecommerce.order.repository.OrderRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.ShopRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.VariantRepository;
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
    private final VariantRepository variantRepository;
    private final ShopRepository shopRepository;

    @Async
    @Transactional
    public void placeOrder(@NotNull PlaceOrderKafkaMessage request) {
        List<OrderItem> orderItems = new ArrayList<>();
        Order newOrder = Order.builder()
                .shopId(shopRepository.findByUuid(UUID.fromString(request.getShopUUID()))
                        .orElseThrow(() -> new ResourceNotFoundException("Shop", "uuid", request.getShopUUID()))
                        .getId())
                .userAccountUUID(UUID.fromString(request.getAccountUUID()))
                .build();
        Order savedOrder = orderRepository.save(newOrder);
        request.getItems().forEach(requestItem -> {
            int decrementStockResult = variantRepository.decrementStockIfAvailableByUUID(UUID.fromString(requestItem.getVariantUUID()), requestItem.getQuantity());
            if (decrementStockResult == 0) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock");
            } else {
                OrderItem newOrderItem = OrderItem.builder()
                        .orderId(savedOrder.getId())
                        .variantId(variantRepository.findByUuid(UUID.fromString(requestItem.getVariantUUID()))
                                .orElseThrow(() -> new ResourceNotFoundException("Variant,", "uuid", requestItem.getVariantUUID()))
                                .getId())
                        .quantity(requestItem.getQuantity())
                        .build();
                orderItems.add(newOrderItem);
            }
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
                .orderId(orderRepository.findByUuid(UUID.fromString(request.getOrderUUID()))
                        .orElseThrow(() -> new ResourceNotFoundException("Order", "uuid", request.getOrderUUID()))
                        .getId())
                .reason(request.getReason())
                .build();
        cancelRequestRepository.save(cancelRequest);
    }

    @Async
    @Transactional
    public void cancelOrder(@NotNull CancelOrderKafkaMessage request) {
        var items = orderItemRepository.findByOrderId(request.getOrderId());
        items.forEach(item -> {
            variantRepository.incrementStockByUUID(item.getUuid(), item.getQuantity());
        });
        orderRepository.deleteById(request.getOrderId());
        orderItemRepository.deleteByOrderId(request.getOrderId());
    }
}
