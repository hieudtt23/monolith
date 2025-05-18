package com.danghieu99.monolith.email.config;

import com.danghieu99.monolith.common.kafka.KafkaConsumerConfig;
import com.danghieu99.monolith.email.dto.kafka.SendEmailKafkaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class EmailKafkaConsumerConfig {

    private final KafkaConsumerConfig kafkaConsumerConfig;

    @Bean("sendEmailListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, SendEmailKafkaRequest> sendEmailListenerFactory() {
        return kafkaConsumerConfig.objConcurrentKafkaListenerContainerFactory();
    }
}
