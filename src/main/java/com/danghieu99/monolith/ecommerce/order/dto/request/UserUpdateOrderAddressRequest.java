package com.danghieu99.monolith.ecommerce.order.dto.request;

import com.danghieu99.monolith.common.dto.BaseRequest;
import com.danghieu99.monolith.ecommerce.order.dto.OrderAddressRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class UserUpdateOrderAddressRequest extends BaseRequest {

    @NotBlank
    private final String orderUUID;

    @NotNull
    private OrderAddressRequest newAddress;
}
