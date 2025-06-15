package com.danghieu99.monolith.ecommerce.product.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    private final String name;

    private final String description;

    private final String imgUrl;
}
