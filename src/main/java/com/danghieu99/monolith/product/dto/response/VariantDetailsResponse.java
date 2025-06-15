package com.danghieu99.monolith.product.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class VariantDetailsResponse {

    @NotBlank
    private final String uuid;

    @NotBlank
    private final String description;

    //type:value
    private Map<@NotBlank String, @NotBlank String> attributes;

    private String imageToken;
}