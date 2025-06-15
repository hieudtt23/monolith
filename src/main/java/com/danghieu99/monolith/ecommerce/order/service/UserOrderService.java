package com.danghieu99.monolith.ecommerce.order.service;

import com.danghieu99.monolith.ecommerce.order.dto.kafka.PlaceOrderKafkaMessage;
import com.danghieu99.monolith.ecommerce.order.dto.kafka.RequestCancelOrderKafkaMessage;
import com.danghieu99.monolith.ecommerce.order.dto.request.CancelOrderRequest;
import com.danghieu99.monolith.ecommerce.order.dto.request.UserUpdateOrderAddressRequest;
import com.danghieu99.monolith.ecommerce.order.dto.request.UserPlaceOrderRequest;
import com.danghieu99.monolith.ecommerce.order.dto.kafka.UpdateOrderAddressKafkaMessage;
import com.danghieu99.monolith.ecommerce.order.dto.response.OrderDetailsResponse;
import com.danghieu99.monolith.ecommerce.order.kafka.producer.RequestCancelOrderKafkaProducer;
import com.danghieu99.monolith.ecommerce.order.kafka.producer.UpdateOrderAddressKafkaProducer;
import com.danghieu99.monolith.ecommerce.order.kafka.producer.PlaceOrderKafkaProducer;
import com.danghieu99.monolith.ecommerce.order.mapper.OrderMapper;
import com.danghieu99.monolith.ecommerce.order.repository.OrderItemRepository;
import com.danghieu99.monolith.ecommerce.order.repository.OrderRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.VariantRepository;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserOrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PlaceOrderKafkaProducer placeOrderKafkaProducer;
    private final OrderItemRepository orderItemRepository;
    private final UpdateOrderAddressKafkaProducer updateOrderAddressKafkaProducer;
    private final RequestCancelOrderKafkaProducer requestCancelOrderKafkaProducer;
    private final VariantRepository variantRepository;

    public List<OrderDetailsResponse> getAllByCurrentUser(@NotNull UserDetailsImpl userDetails) {
        return orderRepository.findByUserAccountUUID(UUID.fromString(userDetails.getUuid()))
                .stream()
                .map(order -> {
                    OrderDetailsResponse orderDetails = orderMapper.toOrderDetailsResponse(order);
                    orderDetails.setItems(orderItemRepository.findByOrderId(order.getId())
                            .stream()
                            .map(orderMapper::toOrderItemResponse)
                            .toList());
                    return orderDetails;
                })
                .toList();
    }

    @Async
    @Transactional
    public void place(@NotNull UserPlaceOrderRequest request,
                      @NotNull UserDetailsImpl userDetails) {
        PlaceOrderKafkaMessage kafkaRequest = orderMapper.toKafkaPlaceOrderRequest(request);
        kafkaRequest.setAccountUUID(userDetails.getUuid());
        placeOrderKafkaProducer.send(kafkaRequest);
    }

    @Async
    @Transactional
    public void updateOrderAddress(@NotNull UserUpdateOrderAddressRequest request,
                                   @NotNull UserDetailsImpl userDetails) {
        UpdateOrderAddressKafkaMessage kafkaRequest = UpdateOrderAddressKafkaMessage.builder()
                .userAccountUUID(userDetails.getUuid())
                .newAddress(request.getNewAddress())
                .build();
        updateOrderAddressKafkaProducer.send(kafkaRequest);
    }

    @Async
    @Transactional
    public void requestCancel(@NotNull CancelOrderRequest request,
                              @NotNull UserDetailsImpl userDetails) {
        RequestCancelOrderKafkaMessage kafkaMessage = RequestCancelOrderKafkaMessage.builder()
                .orderUUID(request.getOrderUUID())
                .accountUUID(userDetails.getUuid())
                .reason(request.getReason())
                .build();
        requestCancelOrderKafkaProducer.send(kafkaMessage);
    }
}