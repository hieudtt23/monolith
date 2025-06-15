package com.danghieu99.monolith.ecommerce.order.controller;

import com.danghieu99.monolith.ecommerce.order.constant.EOrderStatus;
import com.danghieu99.monolith.ecommerce.order.dto.request.ProcessCancelRequestRequest;
import com.danghieu99.monolith.ecommerce.order.dto.response.CancelOrderRequestDetailsResponse;
import com.danghieu99.monolith.ecommerce.order.dto.response.OrderDetailsResponse;
import com.danghieu99.monolith.ecommerce.order.service.SellerOrderService;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seller/order")
@RequiredArgsConstructor
@Validated
public class SellerOrderController {

    private final SellerOrderService sellerOrderService;

    @GetMapping("")
    public Page<OrderDetailsResponse> getAllOrdersByCurrentShop(@NotNull UserDetailsImpl userDetails,
                                                                @NotNull Pageable pageable) {
        return sellerOrderService.getAllOrdersByCurrentShop(userDetails, pageable);
    }

    @GetMapping("/filter-status")
    public Page<OrderDetailsResponse> getAllOrdersByCurrentShopAndStatus(@NotNull UserDetailsImpl userDetails,
                                                                         @NotNull EOrderStatus status,
                                                                         @NotNull Pageable pageable) {
        return sellerOrderService.getAllOrdersByCurrentShopAndStatus(userDetails, status, pageable);
    }

    @GetMapping("/cancel-request")
    public Page<CancelOrderRequestDetailsResponse> getCancelOrderRequestsByCurrentShop(@NotNull UserDetailsImpl userDetails,
                                                                                       @NotNull Pageable pageable) {
        return sellerOrderService.getCancelOrderRequestsByCurrentShop(userDetails, pageable);
    }

    @PostMapping("/cancel-request")
    public void processCancelRequest(ProcessCancelRequestRequest request) {
        sellerOrderService.processCancelRequest(request);
    }
}