package com.danghieu99.monolith.ecommerce.product.dto.response;

import com.danghieu99.monolith.common.dto.BaseResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class GetVariantDetailsResponse extends BaseResponse {

    @NotBlank
    private final String uuid;

    @NotBlank
    private final String description;

    //type:value
    private Map<@NotBlank String, @NotBlank String> attributes;

    private String imageToken;
}