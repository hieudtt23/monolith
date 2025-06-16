package com.danghieu99.monolith.ecommerce.order.controller;

import com.danghieu99.monolith.ecommerce.order.dto.request.CancelOrderRequest;
import com.danghieu99.monolith.ecommerce.order.dto.request.UserPlaceOrderRequest;
import com.danghieu99.monolith.ecommerce.order.dto.request.UserUpdateOrderAddressRequest;
import com.danghieu99.monolith.ecommerce.order.dto.response.OrderDetailsResponse;
import com.danghieu99.monolith.ecommerce.order.service.UserOrderService;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/order")
@RequiredArgsConstructor
@Validated
public class UserOrderController {

    private final UserOrderService userOrderService;

    @GetMapping("")
    public List<OrderDetailsResponse> getAllByCurrentUser(@AuthenticationPrincipal @NotNull UserDetailsImpl userDetails) {
        return userOrderService.getAllByCurrentUser(userDetails);
    }

    @PostMapping("/place")
    @Transactional
    public ResponseEntity<?> place(@RequestBody UserPlaceOrderRequest request,
                                   @AuthenticationPrincipal @NotNull UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userOrderService.place(request, userDetails));
    }

    @PostMapping("/cancel")
    @Transactional
    public void cancel(@RequestParam CancelOrderRequest request,
                       @AuthenticationPrincipal @NotNull UserDetailsImpl userDetails) {
        userOrderService.requestCancel(request, userDetails);
    }

    @PatchMapping("/update-address")
    public void updateOrderAddress(UserUpdateOrderAddressRequest request) {
        userOrderService.updateOrderAddress(request);
    }
}