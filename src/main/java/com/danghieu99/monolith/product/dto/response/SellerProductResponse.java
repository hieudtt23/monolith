package com.danghieu99.monolith.product.dto.response;

import com.danghieu99.monolith.product.entity.Category;
import com.danghieu99.monolith.product.entity.Shop;
import com.danghieu99.monolith.product.entity.Variant;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class SellerProductResponse {

    @NotNull
    private UUID uuid;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private Set<Category> categories;

    @NotNull
    private Shop shop;

    @NotNull
    private BigDecimal basePrice;

    @NotEmpty
    private Set<Variant> variants;

    @NotEmpty
    private Map<String, String> internalAttributes;
}
