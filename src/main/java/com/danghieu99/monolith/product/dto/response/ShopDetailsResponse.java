package com.danghieu99.monolith.product.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopDetailsResponse {

    @NotBlank
    private final String uuid;

    @NotBlank
    private final String name;

    @NotBlank
    private final String description;

}
