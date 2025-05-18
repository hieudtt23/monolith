package com.danghieu99.monolith.order.mapper;

import com.danghieu99.monolith.order.dto.kafka.CancelOrderKafkaMessage;
import com.danghieu99.monolith.order.dto.kafka.PlaceOrderKafkaMessage;
import com.danghieu99.monolith.order.dto.kafka.RequestCancelOrderKafkaMessage;
import com.danghieu99.monolith.order.dto.request.CancelOrderRequest;
import com.danghieu99.monolith.order.dto.request.UserPlaceOrderRequest;
import com.danghieu99.monolith.order.dto.response.CancelOrderRequestDetailsResponse;
import com.danghieu99.monolith.order.dto.response.OrderDetailsResponse;
import com.danghieu99.monolith.order.dto.response.OrderItemResponse;
import com.danghieu99.monolith.order.entity.CancelRequest;
import com.danghieu99.monolith.order.entity.Order;
import com.danghieu99.monolith.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface OrderMapper {

    PlaceOrderKafkaMessage toKafkaPlaceOrderRequest(UserPlaceOrderRequest request);

    CancelOrderKafkaMessage toKafkaCancelOrderRequest(CancelOrderRequest request);

    CancelOrderRequestDetailsResponse toCancelOrderRequestDetails(CancelRequest cancelRequest);

    OrderDetailsResponse toOrderDetailsResponse(Order order);

    OrderItemResponse toOrderItemResponse(OrderItem item);

    RequestCancelOrderKafkaMessage toRequestCancelOrderKafkaMessage(CancelOrderRequest request);
}
