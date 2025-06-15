package com.danghieu99.monolith.ecommerce.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveCategoryRequest {

    @NotBlank
    private final String name;

    @NotBlank
    private final String description;

    private final String superCategoryUUID;
}
