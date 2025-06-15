package com.danghieu99.monolith.ecommerce.product.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UpdateProductDetailsRequest {

    private final String name;

    private final String description;
}
