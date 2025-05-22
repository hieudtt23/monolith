package com.danghieu99.monolith.product.controller;

import com.danghieu99.monolith.product.dto.response.ProductDetailsResponse;
import com.danghieu99.monolith.product.service.product.ProductService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    public Page<ProductDetailsResponse> getAll(Pageable pageable) {
        return productService.getAll(pageable);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getProductDetailsByUUID(@PathVariable @NotBlank String uuid) {
        return ResponseEntity.ok(productService.getProductDetailsByUUID(uuid));
    }

    @GetMapping("/category/id/{id}")
    public Page<ProductDetailsResponse> getByCategoryId(@PathVariable int id, Pageable pageable) {
        return productService.getByCategoryId(id, pageable);
    }


    @GetMapping("/category/uuid/{uuid}")
    public Page<ProductDetailsResponse> getByCategoryUUID(@PathVariable @NotBlank String uuid, Pageable pageable) {
        return productService.getByCategoryUUID(uuid, pageable);
    }

    @GetMapping("/categories")
    public Page<ProductDetailsResponse> getByCategoryUUIDsAny(@RequestParam List<String> categoryUUIDs, Pageable pageable) {
        return productService.getByCategoryUUIDsAny(categoryUUIDs, pageable);
    }

    @GetMapping("/shop/{shopUUID}")
    public Page<ProductDetailsResponse> getByShopUUID(@PathVariable @NotBlank String shopUUID, Pageable pageable) {
        return productService.getByShopUUID(shopUUID, pageable);
    }
}