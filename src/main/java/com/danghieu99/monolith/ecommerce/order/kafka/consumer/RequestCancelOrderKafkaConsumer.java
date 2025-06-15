package com.danghieu99.monolith.ecommerce.order.kafka.consumer;

import com.danghieu99.monolith.ecommerce.order.dto.kafka.RequestCancelOrderKafkaMessage;
import com.danghieu99.monolith.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestCancelOrderKafkaConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "${spring.kafka.topics.order.request.cancel", concurrency = "4", groupId = "request-cancel-order-group")
    public void listen(RequestCancelOrderKafkaMessage message) {
        orderService.placeCancelRequest(message);
    }
}
