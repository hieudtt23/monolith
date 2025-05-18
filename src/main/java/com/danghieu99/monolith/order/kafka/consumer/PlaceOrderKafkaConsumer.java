package com.danghieu99.monolith.order.kafka.consumer;

import com.danghieu99.monolith.order.dto.kafka.PlaceOrderKafkaMessage;
import com.danghieu99.monolith.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceOrderKafkaConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "${spring.kafka.topics.order.place}", concurrency = "4", groupId = "place-order-group")
    public void listen(PlaceOrderKafkaMessage request) {
        orderService.placeOrder(request);
    }

}
