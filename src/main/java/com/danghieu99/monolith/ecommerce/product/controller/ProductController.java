package com.danghieu99.monolith.ecommerce.product.controller;

import com.danghieu99.monolith.ecommerce.product.service.product.ProductService;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    public Page<?> getAll(@ParameterObject Pageable pageable) {
        return productService.getAll(pageable);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getProductDetailsByUUID(@PathVariable @NotBlank String uuid) {
        return ResponseEntity.ok(productService.getProductDetailsByUUID(uuid));
    }

    @GetMapping("/category/id/{id}")
    public Page<?> getByCategoryId(@PathVariable int id, @ParameterObject Pageable pageable) {
        return productService.getByCategoryId(id, pageable);
    }

    @GetMapping("/category/uuid/{uuid}")
    public Page<?> getByCategoryUUID(@PathVariable @NotBlank String uuid, @ParameterObject Pageable pageable) {
        return productService.getByCategoryUUID(uuid, pageable);
    }

    @GetMapping("/categories")
    public Page<?> getByCategoryUUIDsAny(@RequestParam List<@NotBlank String> categoryUUIDs, @ParameterObject Pageable pageable) {
        return productService.getByCategoryUUIDsAny(categoryUUIDs, pageable);
    }

    @GetMapping("/shop/{shopUUID}")
    public Page<?> getByShopUUID(@PathVariable @NotBlank String shopUUID, @ParameterObject Pageable pageable) {
        return productService.getByShopUUID(shopUUID, pageable);
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentlyViewed(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(productService.getRecentlyViewedByAccountUUID(userDetails.getUuid(), pageable));
    }
}