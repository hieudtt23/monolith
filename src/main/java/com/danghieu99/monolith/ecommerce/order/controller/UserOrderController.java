package com.danghieu99.monolith.ecommerce.order.controller;

import com.danghieu99.monolith.ecommerce.order.dto.request.CancelOrderRequest;
import com.danghieu99.monolith.ecommerce.order.dto.request.UserPlaceOrderRequest;
import com.danghieu99.monolith.ecommerce.order.dto.request.UserUpdateOrderAddressRequest;
import com.danghieu99.monolith.ecommerce.order.service.UserOrderService;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/user/order")
@RequiredArgsConstructor
@Validated
public class UserOrderController {

    private final UserOrderService userOrderService;

    @GetMapping("")
    public ResponseEntity<?> getAllByCurrentUser(@AuthenticationPrincipal @NotNull UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userOrderService.getAllByCurrentUser(userDetails));
    }

    @PostMapping("/place")
    @Transactional
    public ResponseEntity<?> placeMultiple(@RequestBody UserPlaceOrderRequest request,
                                           @AuthenticationPrincipal @NotNull UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userOrderService.place(request, userDetails));
    }

    @PostMapping("/place-multiple")
    @Transactional
    public ResponseEntity<?> placeMultiple(@RequestBody @NotEmpty Collection<UserPlaceOrderRequest> requests,
                                           @AuthenticationPrincipal @NotNull UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userOrderService.placeMultiple(requests, userDetails));
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