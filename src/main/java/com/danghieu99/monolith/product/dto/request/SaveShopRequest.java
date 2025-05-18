package com.danghieu99.monolith.product.dto.request;

import com.danghieu99.monolith.product.constant.EShopStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveShopRequest {

    @NotBlank
    private final String name;

    @NotBlank
    private final String description;

    @NotNull
    private final EShopStatus status;
}
