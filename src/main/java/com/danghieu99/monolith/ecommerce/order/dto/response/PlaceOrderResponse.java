package com.danghieu99.monolith.ecommerce.order.dto.response;

import com.danghieu99.monolith.common.dto.BaseResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderResponse extends BaseResponse {

    private List<OrderItemResponse> failed;
}