package com.danghieu99.monolith.ecommerce.order.dto.response;

import com.danghieu99.monolith.ecommerce.order.constant.ECancelStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CancelOrderRequestDetailsResponse {

    @NotBlank
    private String userAccountUUID;

    @NotBlank
    private String orderUUID;

    @NotBlank
    private String shopUUID;

    @NotBlank
    private String reason;

    @NotNull
    private ECancelStatus status;
}
