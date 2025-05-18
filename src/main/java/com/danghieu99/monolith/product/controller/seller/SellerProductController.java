package com.danghieu99.monolith.product.controller.seller;

import com.danghieu99.monolith.product.dto.request.SaveProductRequest;
import com.danghieu99.monolith.product.dto.request.SaveVariantRequest;
import com.danghieu99.monolith.product.dto.request.UpdateProductDetailsRequest;
import com.danghieu99.monolith.product.dto.response.VariantDetailsResponse;
import com.danghieu99.monolith.product.dto.response.ProductDetailsResponse;
import com.danghieu99.monolith.product.service.product.SellerProductService;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller/product")
@Validated
public class SellerProductController {

    private final SellerProductService sellerProductService;

    @GetMapping("")
    public Page<ProductDetailsResponse> getAllByCurrentShop(@AuthenticationPrincipal @NotNull UserDetailsImpl userDetails,
                                                            @PageableDefault Pageable pageable) {
        return sellerProductService.getAllByCurrentShop(userDetails, pageable);
    }

    @PostMapping("")
    public ResponseEntity<?> addToCurrentShop(@AuthenticationPrincipal @NotNull UserDetailsImpl userDetails,
                                              @RequestBody @NotNull SaveProductRequest request) {
        sellerProductService.addToCurrentShop(userDetails, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("")
    public ResponseEntity<?> updateProductDetailsByUUID(@RequestParam @NotBlank String uuid, @RequestBody UpdateProductDetailsRequest request) {
        sellerProductService.updateProductDetailsByUUID(uuid, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteProductByUUID(@RequestParam @NotBlank String uuid) {
        sellerProductService.deleteProductByUUID(uuid);
        return ResponseEntity.ok().build();
    }
}
