package com.danghieu99.monolith.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteAttributeRequest {

    @NotBlank
    private final String productUUID;

    @NotBlank
    private final String type;

    @NotBlank
    private final String value;
}
