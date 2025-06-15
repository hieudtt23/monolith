package com.danghieu99.monolith.ecommerce.order.dto.request;

import com.danghieu99.monolith.ecommerce.order.dto.OrderItem;
import com.danghieu99.monolith.ecommerce.order.dto.ShippingAddress;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserPlaceOrderRequest {

    @NotEmpty
    private final String shopUUID;

    @NotNull
    private final ShippingAddress address;

    @NotNull
    private String shipmentProviderUUID;

    @NotEmpty
    private final List<OrderItem> items;
}