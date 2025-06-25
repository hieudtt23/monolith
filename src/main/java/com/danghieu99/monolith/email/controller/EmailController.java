package com.danghieu99.monolith.email.controller;

import com.danghieu99.monolith.email.dto.SendEmailRequest;
import com.danghieu99.monolith.email.service.SendEmailToKafkaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
@Validated
public class EmailController {

    private final SendEmailToKafkaService sendEmailToKafkaService;

    @PostMapping("/send")
    public ResponseEntity<?> send(@Valid @RequestBody SendEmailRequest sendEmailRequest) {
        sendEmailToKafkaService.send(sendEmailRequest);
        return ResponseEntity.ok().build();
    }
}