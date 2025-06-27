package com.danghieu99.monolith.ecommerce.order.service;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.ecommerce.order.constant.ECancelStatus;
import com.danghieu99.monolith.ecommerce.order.constant.EOrderStatus;
import com.danghieu99.monolith.ecommerce.order.dto.request.ProcessCancelRequestRequest;
import com.danghieu99.monolith.ecommerce.order.dto.response.CancelOrderRequestDetailsResponse;
import com.danghieu99.monolith.ecommerce.order.dto.response.OrderDetailsResponse;
import com.danghieu99.monolith.ecommerce.order.entity.CancelRequest;
import com.danghieu99.monolith.ecommerce.order.entity.Order;
import com.danghieu99.monolith.ecommerce.order.entity.OrderItem;
import com.danghieu99.monolith.ecommerce.order.mapper.OrderMapper;
import com.danghieu99.monolith.ecommerce.order.repository.CancelRequestRepository;
import com.danghieu99.monolith.ecommerce.order.repository.OrderItemRepository;
import com.danghieu99.monolith.ecommerce.order.repository.OrderRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.VariantRepository;
import com.danghieu99.monolith.email.dto.SendEmailRequest;
import com.danghieu99.monolith.email.repository.EmailTemplateRepository;
import com.danghieu99.monolith.email.service.SendEmailToKafkaService;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import com.danghieu99.monolith.security.entity.Account;
import com.danghieu99.monolith.security.repository.jpa.AccountRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerOrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CancelRequestRepository cancelRequestRepository;
    private final AccountRepository accountRepository;
    private final SendEmailToKafkaService sendEmailToKafkaService;
    private final EmailTemplateRepository emailTemplateRepository;
    private final VariantRepository variantRepository;
    private final OrderItemRepository orderItemRepository;

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

    //process refund
    @Async
    @Transactional
    public void processCancelRequest(@NotNull ProcessCancelRequestRequest request) {
        CancelRequest cancelRequest = cancelRequestRepository.findByUuid(UUID.fromString(request.getCancelRequestUUID()))
                .orElseThrow(() -> new ResourceNotFoundException("CancelRequest", "uuid", request.getCancelRequestUUID()));
        Account account = accountRepository.findByUuid(cancelRequest.getUserAccountUUID())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "uuid", cancelRequest.getUserAccountUUID()));
        Order order = orderRepository.findById(cancelRequest.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", cancelRequest.getOrderId()));
        Collection<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        if (request.isAccept()) {
            cancelRequestRepository.updateStatusByUuid(cancelRequest.getUuid(), ECancelStatus.ACCEPTED);
            orderRepository.updateOrderStatusByUUID(cancelRequest.getUuid(), EOrderStatus.CANCELED);
            orderItems.forEach(orderItem -> {
                variantRepository.incrementStockById(orderItem.getVariantId(), orderItem.getQuantity());
            });
        } else {
            cancelRequestRepository.updateStatusByUuid(cancelRequest.getUuid(), ECancelStatus.DENIED);
        }
        if (emailTemplateRepository.findByName("cancelOrder").isPresent()) {
            sendEmailToKafkaService.send(SendEmailRequest.builder()
                    .to(List.of(account.getEmail()))
                    .templateName("cancelOrder")
                    .templateParams(Map.of(
                            "orderNumber", order.getUuid().toString(),
                            "userName", account.getUsername(),
                            "status", cancelRequest.getStatus().name()
                    )).build());
        } else {
            sendEmailToKafkaService.send(SendEmailRequest.builder()
                    .to(List.of(account.getEmail()))
                    .subject("Order Cancel Success")
                    .plainText("Order " + order.getUuid() + ", userName " + account.getUsername() + ", status " + cancelRequest.getStatus().name() + " has been placed.")
                    .build());
        }
    }
}