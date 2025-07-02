package com.danghieu99.monolith.ecommerce.product.dto.response;

import com.danghieu99.monolith.common.dto.BaseResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class GetShopDetailsResponse extends BaseResponse {

    @NotBlank
    private final String uuid;

    @NotBlank
    private final String name;

    @NotBlank
    private final String description;

}
