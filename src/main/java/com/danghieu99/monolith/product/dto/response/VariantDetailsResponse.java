package com.danghieu99.monolith.product.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty
    private final Map<@NotBlank String, @NotBlank String> attributes;

    private final String imageUrl;
}