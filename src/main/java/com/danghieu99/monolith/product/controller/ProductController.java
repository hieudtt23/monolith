package com.danghieu99.monolith.product.controller;

import com.danghieu99.monolith.product.service.product.ProductService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<?> getAllProducts(@RequestParam(required = false) @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(productService.getAll(pageable));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getProductDetailsByUUID(@PathVariable @NotBlank String uuid) {
        return ResponseEntity.ok(productService.getProductDetailsByUUID(uuid));
    }
}