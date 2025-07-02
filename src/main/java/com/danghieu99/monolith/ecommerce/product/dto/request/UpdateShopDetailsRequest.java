package com.danghieu99.monolith.ecommerce.product.dto.request;

import com.danghieu99.monolith.common.dto.BaseRequest;
import com.danghieu99.monolith.ecommerce.product.constant.EShopStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class UpdateShopDetailsRequest extends BaseRequest {

    private final String name;

    private final String description;

    private final EShopStatus status;

//    private final String imgUrl;
}
