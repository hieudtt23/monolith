package com.danghieu99.monolith.ecommerce.product.controller;

import com.danghieu99.monolith.ecommerce.product.dto.response.GetShopDetailsResponse;
import com.danghieu99.monolith.ecommerce.product.service.shop.ShopService;
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
    public GetShopDetailsResponse getByUUID(@RequestParam @NotBlank String uuid) {
        return shopService.getByUUID(uuid);
    }

    @GetMapping("/name")
    public GetShopDetailsResponse getByName(@RequestParam @NotBlank String name) {
        return shopService.getByName(name);
    }

    @GetMapping("/search-name")
    public Page<GetShopDetailsResponse> getByNameContainingIgnoreCase(@RequestParam @NotBlank String name, @RequestParam @PageableDefault Pageable pageable) {
        return shopService.getByNameContaining(name, pageable);
    }
}
