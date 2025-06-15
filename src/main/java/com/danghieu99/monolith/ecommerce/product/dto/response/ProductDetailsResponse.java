package com.danghieu99.monolith.ecommerce.product.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder
public class ProductDetailsResponse {

    @NotNull
    private String uuid;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private long basePrice;

    @NotNull
    private String status;

    @NotNull
    private String shopUUID;

    private List<@NotBlank String> categories;

    private List<@NotNull VariantDetailsResponse> variants;

    private List<@NotBlank String> imageTokens;
}