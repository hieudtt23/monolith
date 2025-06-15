package com.danghieu99.monolith.ecommerce.order.kafka.consumer;

import com.danghieu99.monolith.ecommerce.order.dto.kafka.UpdateOrderAddressKafkaMessage;
import com.danghieu99.monolith.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateOrderAddressConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "${spring.kafka.topics.order.update.address}", concurrency = "4", groupId = "update-order-address-group")
    public void listen(UpdateOrderAddressKafkaMessage request) {
        orderService.updateAddress(request);
    }
}
