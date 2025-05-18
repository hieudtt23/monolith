package com.danghieu99.monolith.email.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadAttachmentFileService {

    String uploadFile(MultipartFile file);

}