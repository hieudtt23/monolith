package com.danghieu99.monolith.ecommerce.product.dto.request;

import com.danghieu99.monolith.common.dto.BaseRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class SaveVariantRequest extends BaseRequest {

    @NotEmpty
    private Map<String, String> attributes;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer stock;
}
