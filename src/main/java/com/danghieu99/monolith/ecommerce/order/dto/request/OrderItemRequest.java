package com.danghieu99.monolith.ecommerce.order.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemRequest {

    @NotEmpty
    private final String productUUID;

    @NotEmpty
    private final String variantUUID;

    @NotNull
    private final int quantity;
}
