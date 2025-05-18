package com.danghieu99.monolith.email.kafka;

import com.danghieu99.monolith.email.dto.kafka.SendEmailKafkaRequest;
import com.danghieu99.monolith.email.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailKafkaConsumer {

    private final SendEmailService sendEmailService;

    @KafkaListener(topics = "${spring.kafka.topics.email.send}",
            groupId = "group-email-worker",
            concurrency = "4",
            containerFactory = "sendEmailListenerFactory")
    public void listen(SendEmailKafkaRequest request) {
        sendEmailService.send(request);
    }
}