package com.danghieu99.monolith.product.dto.request;

import com.danghieu99.monolith.product.constant.EShopStatus;
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
