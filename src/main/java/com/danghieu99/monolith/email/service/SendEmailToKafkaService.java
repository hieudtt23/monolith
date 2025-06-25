package com.danghieu99.monolith.email.service;

import com.danghieu99.monolith.email.dto.SendEmailRequest;
import com.danghieu99.monolith.email.dto.kafka.SendEmailKafkaRequest;
import com.danghieu99.monolith.email.dto.kafka.SendEmailKafkaRequestAttachment;
import com.danghieu99.monolith.email.kafka.SendEmailKafkaProducer;
import com.danghieu99.monolith.email.mapper.EmailMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SendEmailToKafkaService {

    private final SendEmailKafkaProducer producer;
    private final EmailMapper emailMapper;
    private final UploadAttachmentFileService uploadAttachmentFileService;

    public SendEmailToKafkaService(SendEmailKafkaProducer producer,
                                   EmailMapper emailMapper,
                                   @Qualifier("email-cloudinaryUploadFileService") UploadAttachmentFileService uploadAttachmentFileService) {
        this.producer = producer;
        this.emailMapper = emailMapper;
        this.uploadAttachmentFileService = uploadAttachmentFileService;
    }

    @Async
    @Transactional
    public void send(SendEmailRequest request) {
        SendEmailKafkaRequest kafkaRequest = emailMapper.toSendEmailKafkaRequest(request);
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            kafkaRequest.setAttachments(request.getFiles().parallelStream().map(file -> {
                String url = this.uploadAttachment(file);
                return SendEmailKafkaRequestAttachment.builder()
                        .fileName(file.getName())
                        .contentType(file.getContentType())
                        .fileUrl(url)
                        .build();
            }).toList());
        }
        producer.send(kafkaRequest);
    }

    private String uploadAttachment(MultipartFile file) {
        return uploadAttachmentFileService.uploadFile(file);
    }
}