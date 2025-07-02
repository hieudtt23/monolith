package com.danghieu99.monolith.ecommerce.product.dto.request;

import com.danghieu99.monolith.common.dto.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder(access = AccessLevel.PUBLIC)
public class SaveVariantImageRequest extends BaseRequest {

    @NotBlank
    private final String variantUUID;

    @NotBlank
    private final String imageToken;
}
