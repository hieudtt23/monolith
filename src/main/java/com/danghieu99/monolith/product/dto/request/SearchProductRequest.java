package com.danghieu99.monolith.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class SearchProductRequest {

    @NotBlank
    @Length(min = 3, max = 50)
    private String name;

    private Set<String> categories;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;
}
