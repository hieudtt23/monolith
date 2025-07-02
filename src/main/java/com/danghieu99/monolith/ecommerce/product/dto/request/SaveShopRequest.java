package com.danghieu99.monolith.ecommerce.product.dto.request;

import com.danghieu99.monolith.common.dto.BaseRequest;
import com.danghieu99.monolith.ecommerce.product.constant.EShopStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class SaveShopRequest extends BaseRequest {

    @NotBlank
    private final String name;

    @NotBlank
    private final String description;

    @NotNull
    private final EShopStatus status;
}
