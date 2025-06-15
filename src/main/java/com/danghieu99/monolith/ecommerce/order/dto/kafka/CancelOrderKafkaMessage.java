package com.danghieu99.monolith.ecommerce.order.dto.kafka;

import com.danghieu99.monolith.common.dto.BaseKafkaRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Getter
@Setter
public class CancelOrderKafkaMessage extends BaseKafkaRequest {

    @NotBlank
    private int orderId;

    @NotBlank
    private String reason;
}