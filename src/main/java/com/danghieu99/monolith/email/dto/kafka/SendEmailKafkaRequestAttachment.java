package com.danghieu99.monolith.email.dto.kafka;

import com.danghieu99.monolith.common.dto.BaseKafkaRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Getter
@Setter
public class SendEmailKafkaRequestAttachment extends BaseKafkaRequest {

    @NotBlank
    private String fileName;

    @NotBlank
    private String contentType;

    @NotBlank
    private String fileUrl;
}
