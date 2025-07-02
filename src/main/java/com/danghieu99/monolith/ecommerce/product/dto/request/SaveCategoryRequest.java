package com.danghieu99.monolith.ecommerce.product.dto.request;

import com.danghieu99.monolith.common.dto.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class SaveCategoryRequest extends BaseRequest {

    @NotBlank
    private final String name;

    @NotBlank
    private final String description;

    private final String superCategoryUUID;
}
