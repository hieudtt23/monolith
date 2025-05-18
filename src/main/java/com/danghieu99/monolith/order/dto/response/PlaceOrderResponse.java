package com.danghieu99.monolith.order.dto.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceOrderResponse {

    @NotNull
    private final boolean success;

    @NotEmpty
    private final String message;
}
