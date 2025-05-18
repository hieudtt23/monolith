package com.danghieu99.monolith.product.controller;

import com.danghieu99.monolith.product.dto.response.ShopDetailsResponse;
import com.danghieu99.monolith.product.service.shop.ShopService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor
@Validated
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/uuid")
    public ShopDetailsResponse getByUUID(@RequestParam @NotBlank String uuid) {
        return shopService.getByUUID(uuid);
    }

    @GetMapping("/name")
    public ShopDetailsResponse getByName(@RequestParam @NotBlank String name) {
        return shopService.getByName(name);
    }

    @GetMapping("/search-name")
    public Page<ShopDetailsResponse> getByNameContainingIgnoreCase(@RequestParam @NotBlank String name, @RequestParam @PageableDefault Pageable pageable) {
        return shopService.getByNameContaining(name, pageable);
    }
}
