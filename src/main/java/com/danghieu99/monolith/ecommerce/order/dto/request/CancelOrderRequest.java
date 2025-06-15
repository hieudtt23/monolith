package com.danghieu99.monolith.ecommerce.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CancelOrderRequest {

    @NotBlank
    private final String orderUUID;

    @NotBlank
    private final String reason;
}
