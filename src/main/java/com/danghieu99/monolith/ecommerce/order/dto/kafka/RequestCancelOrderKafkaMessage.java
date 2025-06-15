package com.danghieu99.monolith.ecommerce.order.dto.kafka;

import com.danghieu99.monolith.common.dto.BaseKafkaRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class RequestCancelOrderKafkaMessage extends BaseKafkaRequest {

    @NotBlank
    private String accountUUID;

    @NotBlank
    private String orderUUID;

    @NotBlank
    private String reason;
}
