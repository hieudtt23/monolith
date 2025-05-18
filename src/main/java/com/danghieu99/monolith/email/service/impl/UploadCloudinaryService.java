package com.danghieu99.monolith.email.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.danghieu99.monolith.email.service.UploadAttachmentFileService;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Service("email-cloudinaryUploadFileService")
public class UploadCloudinaryService implements UploadAttachmentFileService {

    private final Cloudinary cloudinary;

    public UploadCloudinaryService(@Qualifier("email-cloudinary") Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @SneakyThrows
    public String uploadFile(@NotNull MultipartFile file) {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "public_id", "email_attachment_"
                        + Instant.now().toEpochMilli()
                        + "_" + Objects.requireNonNull(file.getName())
                        .replaceAll("[^a-zA-Z0-9.\\-]", "_"),
                "resource_type", file.getContentType() != null ? file.getContentType() : "auto"
        ));
        return (String) uploadResult.get("url");
    }
}