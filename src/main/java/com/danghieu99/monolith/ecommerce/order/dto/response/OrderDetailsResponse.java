package com.danghieu99.monolith.ecommerce.order.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDetailsResponse {

    @NotBlank
    private String shopUUID;

    @NotBlank
    private String userAccountUUID;

    @NotBlank
    private String status;

    @NotEmpty
    private List<OrderItemResponse> items;
}
