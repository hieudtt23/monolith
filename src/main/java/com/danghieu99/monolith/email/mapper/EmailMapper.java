package com.danghieu99.monolith.email.mapper;

import com.danghieu99.monolith.email.dto.SendEmailRequest;
import com.danghieu99.monolith.email.dto.kafka.SendEmailKafkaRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface EmailMapper {

    SendEmailKafkaRequest toSendEmailKafkaRequest(SendEmailRequest sendEmailRequest);
}
