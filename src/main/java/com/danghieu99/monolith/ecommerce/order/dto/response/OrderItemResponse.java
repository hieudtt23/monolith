package com.danghieu99.monolith.ecommerce.order.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {

    @NotBlank
    private final String variantUUID;

    @NotNull
    private final int quantity;
}
