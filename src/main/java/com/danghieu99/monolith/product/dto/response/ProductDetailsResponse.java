package com.danghieu99.monolith.product.dto.response;

import com.danghieu99.monolith.product.entity.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder
public class ProductDetailsResponse {

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

    @NotEmpty
    private Set<VariantDetailsResponse> variants;
}
