package com.danghieu99.monolith.ecommerce.product.dto.request;

import com.danghieu99.monolith.ecommerce.product.constant.EShopStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateShopDetailsRequest {

    private final String name;

    private final String description;

    private final EShopStatus status;

//    private final String imgUrl;
}
