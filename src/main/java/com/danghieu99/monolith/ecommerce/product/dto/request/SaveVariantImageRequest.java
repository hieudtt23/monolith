package com.danghieu99.monolith.ecommerce.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PUBLIC)
public class SaveVariantImageRequest {

    @NotBlank
    private final String variantUUID;

    @NotBlank
    private final String imageToken;
}
