package com.danghieu99.monolith.order.config;

import com.danghieu99.monolith.common.kafka.KafkaConsumerConfig;
import com.danghieu99.monolith.order.dto.kafka.CancelOrderKafkaMessage;
import com.danghieu99.monolith.order.dto.kafka.PlaceOrderKafkaMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
@RequiredArgsConstructor
public class OrderKafkaConsumerConfig {

    private final KafkaConsumerConfig kafkaConsumerConfig;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PlaceOrderKafkaMessage> placeOrderKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.objConcurrentKafkaListenerContainerFactory();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CancelOrderKafkaMessage> cancelOrderKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.objConcurrentKafkaListenerContainerFactory();
    }
}
