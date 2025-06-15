package com.danghieu99.monolith.ecommerce.order.dto.kafka;

import com.danghieu99.monolith.common.dto.BaseKafkaRequest;
import com.danghieu99.monolith.ecommerce.order.dto.ShippingAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class UpdateOrderAddressKafkaMessage extends BaseKafkaRequest {

    @NotBlank
    private String orderUUID;

    @NotBlank
    private String userAccountUUID;

    @NotNull
    private ShippingAddress newAddress;
}
