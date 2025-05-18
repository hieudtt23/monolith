package com.danghieu99.monolith.cart.dto;

import com.danghieu99.monolith.common.dto.BaseRequest;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
public class GetCartResponse extends BaseRequest {

    //variantUUID:quantity
    private Map<String, Integer> items;
}
