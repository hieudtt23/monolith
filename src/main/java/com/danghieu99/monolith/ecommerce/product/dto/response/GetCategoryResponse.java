package com.danghieu99.monolith.ecommerce.product.dto.response;

import com.danghieu99.monolith.common.dto.BaseResponse;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class GetCategoryResponse extends BaseResponse {

    private final String name;

    private final String description;

    private final String imgUrl;
}
