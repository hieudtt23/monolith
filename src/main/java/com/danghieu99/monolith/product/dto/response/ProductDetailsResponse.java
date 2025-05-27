package com.danghieu99.monolith.product.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty
    private List<@NotBlank String> categories;

    @NotEmpty
    private List<@NotNull VariantDetailsResponse> variants;

    @NotEmpty
    private List<@NotBlank String> imageToken;
}
