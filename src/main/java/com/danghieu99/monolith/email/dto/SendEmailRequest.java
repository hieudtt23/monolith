package com.danghieu99.monolith.email.dto;

import com.danghieu99.monolith.common.dto.BaseRequest;
import com.danghieu99.monolith.common.validate.ValidMultipartFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class SendEmailRequest extends BaseRequest {

    @NotEmpty
    private List<@NotBlank String> from;

    @NotEmpty
    private List<@NotBlank @Email String> to;

    private List<@NotBlank @Email String> cc;

    private List<@NotBlank @Email String> bcc;

    private String subject;

    private String plainText;

    private String html;

    private String templateName;

    @Size(min = 1)
    private Map<@NotBlank String, @NotBlank String> templateParams;

    @Size(min = 1, max = 5)
    private List<@ValidMultipartFile(maxSize = 26214400, allowedTypes = {}) MultipartFile> files;
}