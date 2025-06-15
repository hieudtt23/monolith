package com.danghieu99.monolith.ecommerce.product.controller.seller;

import com.danghieu99.monolith.ecommerce.product.dto.request.SaveShopRequest;
import com.danghieu99.monolith.ecommerce.product.dto.request.UpdateShopDetailsRequest;
import com.danghieu99.monolith.ecommerce.product.dto.response.ShopDetailsResponse;
import com.danghieu99.monolith.ecommerce.product.service.shop.SellerShopService;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seller/shop")
@RequiredArgsConstructor
@Validated
public class SellerShopController {

    private final SellerShopService sellerShopService;

    @PostMapping("")
    public ShopDetailsResponse create(@AuthenticationPrincipal @NotNull UserDetailsImpl userDetails,
                                      @NotNull @RequestParam SaveShopRequest request) {
        return sellerShopService.createCurrentUserShop(userDetails, request);
    }

    @DeleteMapping("")
    public ResponseEntity<?> delete(@AuthenticationPrincipal @NotNull UserDetailsImpl userDetails) {
        sellerShopService.deleteCurrentUserShop(userDetails);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("")
    public ShopDetailsResponse editDetails(@AuthenticationPrincipal @NotNull UserDetailsImpl userDetails,
                                           UpdateShopDetailsRequest request) {
        return sellerShopService.editCurrentUserShopDetails(userDetails, request);
    }
}