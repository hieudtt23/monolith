package com.danghieu99.monolith.order.kafka.consumer;


import com.danghieu99.monolith.order.dto.kafka.CancelOrderKafkaMessage;
import com.danghieu99.monolith.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelOrderKafkaConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "${spring.kafka.topics.order.cancel}", concurrency = "4", groupId = "cancel-order-group")
    public void listen(CancelOrderKafkaMessage request) {
        orderService.cancelOrder(request);
    }
}
