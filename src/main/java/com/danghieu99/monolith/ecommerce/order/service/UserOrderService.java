package com.danghieu99.monolith.ecommerce.order.service;

import com.danghieu99.monolith.common.exception.ResourceNotAvailableException;
import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.ecommerce.order.dto.request.CancelOrderRequest;
import com.danghieu99.monolith.ecommerce.order.dto.request.UserUpdateOrderAddressRequest;
import com.danghieu99.monolith.ecommerce.order.dto.request.UserPlaceOrderRequest;
import com.danghieu99.monolith.ecommerce.order.dto.response.OrderDetailsResponse;
import com.danghieu99.monolith.ecommerce.order.dto.response.OrderItemResponse;
import com.danghieu99.monolith.ecommerce.order.dto.response.PlaceOrderResponse;
import com.danghieu99.monolith.ecommerce.order.entity.CancelRequest;
import com.danghieu99.monolith.ecommerce.order.entity.Order;
import com.danghieu99.monolith.ecommerce.order.entity.OrderAddress;
import com.danghieu99.monolith.ecommerce.order.entity.OrderItem;
import com.danghieu99.monolith.ecommerce.order.mapper.OrderMapper;
import com.danghieu99.monolith.ecommerce.order.repository.CancelRequestRepository;
import com.danghieu99.monolith.ecommerce.order.repository.OrderAddressRepository;
import com.danghieu99.monolith.ecommerce.order.repository.OrderItemRepository;
import com.danghieu99.monolith.ecommerce.order.repository.OrderRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.ProductRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.ShopRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.VariantRepository;
import com.danghieu99.monolith.email.dto.SendEmailRequest;
import com.danghieu99.monolith.email.repository.EmailTemplateRepository;
import com.danghieu99.monolith.email.service.SendEmailToKafkaService;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import com.danghieu99.monolith.security.repository.jpa.AccountRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserOrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final VariantRepository variantRepository;
    private final ShopRepository shopRepository;
    private final OrderAddressRepository orderAddressRepository;
    private final CancelRequestRepository cancelRequestRepository;
    private final SendEmailToKafkaService sendEmailToKafkaService;
    private final AccountRepository accountRepository;
    private final EmailTemplateRepository emailTemplateRepository;

    public List<OrderDetailsResponse> getAllByCurrentUser(@NotNull UserDetailsImpl userDetails) {
        return orderRepository.findByUserAccountUUID(UUID.fromString(userDetails.getUuid())).stream().map(order -> {
            OrderDetailsResponse orderResponse = orderMapper.toOrderDetailsResponse(order);
            orderResponse.setItems(orderItemRepository.findByOrderId(order.getId()).stream().map(orderMapper::toOrderItemResponse).toList());
            return orderResponse;
        }).toList();
    }

    @Transactional
    public PlaceOrderResponse place(@NotNull UserPlaceOrderRequest request, @NotNull UserDetailsImpl userDetails) {
        Order savedOrder = orderRepository.save(Order.builder()
                .shopId(shopRepository.findByUuid(UUID.fromString(request.getShopUUID()))
                        .orElseThrow(() -> new ResourceNotFoundException("Shop", "uuid", request.getShopUUID()))
                        .getId())
                .userAccountUUID(UUID.fromString(userDetails.getUuid()))
                .build());
        orderAddressRepository.save(orderMapper.toOrderAddress(request.getAddress()));
        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemResponse> failed = new ArrayList<>();
        request.getItems().forEach(requestItem -> {
            try {
                int decrementStockResult = variantRepository.decrementStockIfAvailableByUUID(UUID.fromString(requestItem.getVariantUUID()), requestItem.getQuantity());
                if (decrementStockResult == 0) {
                    throw new ResourceNotAvailableException("Variant", "uuid", requestItem.getVariantUUID());
                }
                OrderItem newOrderItem = OrderItem.builder()
                        .orderId(savedOrder.getId())
                        .productId(productRepository.findByUuid(UUID.fromString(requestItem.getProductUUID()))
                                .orElseThrow(() -> new ResourceNotFoundException("Product", "uuid", requestItem.getProductUUID()))
                                .getId())
                        .variantId(variantRepository.findByUuid(UUID.fromString(requestItem.getVariantUUID()))
                                .orElseThrow(() -> new ResourceNotFoundException("Variant,", "uuid", requestItem.getVariantUUID()))
                                .getId())
                        .quantity(requestItem.getQuantity())
                        .build();
                orderItems.add(newOrderItem);
            } catch (Exception e) {
                log.error(e.getMessage());
                failed.add(orderMapper.toOrderItemResponse(requestItem));
            }
        });
        var savedItems = orderItemRepository.saveAll(orderItems);
        if (savedItems.isEmpty()) {
            throw new RuntimeException("No order items saved");
        }
        if (emailTemplateRepository.findByName("placeOrder").isPresent()) {
            sendEmailToKafkaService.send(SendEmailRequest.builder()
                    .to(List.of(accountRepository.findEmailByAccountUUID(UUID.fromString(userDetails.getUuid()))))
                    .templateName("placeOrder")
                    .templateParams(Map.of(
                            "orderNumber", savedOrder.getUuid().toString(),
                            "userName", userDetails.getUsername(),
                            "address", request.getAddress().toString()
                    )).build());
        } else {
            sendEmailToKafkaService.send(SendEmailRequest.builder()
                    .to(List.of(accountRepository.findEmailByAccountUUID(UUID.fromString(userDetails.getUuid()))))
                    .subject("Order Placement Successful")
                    .plainText("Order " + savedOrder.getUuid() + ", userName " + userDetails.getUsername() + ", Address " + request.getAddress() + " has been placed.")
                    .build());
        }
        PlaceOrderResponse response = new PlaceOrderResponse();
        if (!failed.isEmpty()) {
            response.setFailed(failed);
        }
        return response;
    }

    @Async
    @Transactional
    public void updateOrderAddress(@NotNull UserUpdateOrderAddressRequest request) {
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
    public void requestCancel(@NotNull CancelOrderRequest request, @NotNull UserDetailsImpl userDetails) {
        CancelRequest cancelRequest = CancelRequest.builder()
                .userAccountUUID(UUID.fromString(userDetails.getUuid()))
                .orderId(orderRepository.findByUuid(UUID.fromString(request.getOrderUUID()))
                        .orElseThrow(() -> new ResourceNotFoundException("Order", "uuid", request.getOrderUUID()))
                        .getId())
                .reason(request.getReason())
                .build();
        cancelRequestRepository.save(cancelRequest);
    }
}