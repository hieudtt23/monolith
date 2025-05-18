package com.danghieu99.monolith.order.dto.request;

import com.danghieu99.monolith.order.constant.EOrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellerUpdateOrderRequest {

    @NotBlank
    private final String orderUUID;

    @NotNull
    private final EOrderStatus status;

    @NotBlank
    private final String details;
}
