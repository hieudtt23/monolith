package com.danghieu99.monolith.order.dto.kafka;

import com.danghieu99.monolith.common.dto.BaseKafkaRequest;
import com.danghieu99.monolith.order.dto.ShippingAddress;
import com.danghieu99.monolith.order.dto.OrderItem;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Getter
@Setter
public class PlaceOrderKafkaMessage extends BaseKafkaRequest {

    @NotEmpty
    private String shopUUID;

    @NotNull
    private ShippingAddress address;

    @NotNull
    private String shipmentProviderUUID;

    @NotEmpty
    private List<OrderItem> items;

    @NotEmpty
    private String accountUUID;
}
