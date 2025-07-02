package com.danghieu99.monolith.email.dto.kafka;

import com.danghieu99.monolith.common.dto.BaseKafkaRequest;
import lombok.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Getter
@Setter
public class SendEmailKafkaRequest extends BaseKafkaRequest {

    private String fromAddress;

    private String fromName;

    private String[] to;

    private String[] cc;

    private String[] bcc;

    private String subject;

    private String plainText;

    private String html;

    private String templateName;

    private Map<String, String> templateParams;

    private List<SendEmailKafkaRequestAttachment> files;
}