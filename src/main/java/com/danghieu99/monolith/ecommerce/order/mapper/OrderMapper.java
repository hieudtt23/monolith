package com.danghieu99.monolith.ecommerce.order.mapper;

import com.danghieu99.monolith.ecommerce.order.dto.request.OrderItemRequest;
import com.danghieu99.monolith.ecommerce.order.dto.response.CancelOrderRequestDetailsResponse;
import com.danghieu99.monolith.ecommerce.order.dto.response.OrderDetailsResponse;
import com.danghieu99.monolith.ecommerce.order.dto.response.OrderItemResponse;
import com.danghieu99.monolith.ecommerce.order.entity.CancelRequest;
import com.danghieu99.monolith.ecommerce.order.entity.Order;
import com.danghieu99.monolith.ecommerce.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface OrderMapper {

    CancelOrderRequestDetailsResponse toCancelOrderRequestDetails(CancelRequest cancelRequest);

    OrderDetailsResponse toOrderDetailsResponse(Order order);

    OrderItemResponse toOrderItemResponse(OrderItem item);

    OrderItemResponse toOrderItemResponse(OrderItemRequest request);
}
