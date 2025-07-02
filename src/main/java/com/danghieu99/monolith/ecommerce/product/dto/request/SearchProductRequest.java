package com.danghieu99.monolith.ecommerce.product.dto.request;

import com.danghieu99.monolith.common.dto.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class SearchProductRequest extends BaseRequest {

    @NotBlank
    @Length(min = 3, max = 50)
    private String name;

    private Set<String> categories;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;
}
