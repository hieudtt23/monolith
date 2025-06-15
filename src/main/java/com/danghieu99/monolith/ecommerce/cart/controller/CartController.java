package com.danghieu99.monolith.ecommerce.cart.controller;

import com.danghieu99.monolith.ecommerce.cart.dto.GetCartResponse;
import com.danghieu99.monolith.ecommerce.cart.service.UserCartService;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/cart")
@RequiredArgsConstructor
@Validated
public class CartController {

    private final UserCartService userCartService;

    @GetMapping("")
    public GetCartResponse getCart(@AuthenticationPrincipal @NotNull UserDetailsImpl userDetails) {
        return userCartService.getCart(userDetails);
    }

    @PostMapping("")
    public ResponseEntity<?> setItemQuantity(@AuthenticationPrincipal @NotNull UserDetailsImpl userDetails,
                                             @RequestParam @NotBlank String variantUUID,
                                             @RequestParam int quantity) {
        userCartService.setItemQuantity(userDetails, variantUUID, quantity);
        return ResponseEntity.ok().build();
    }
}
