package com.danghieu99.monolith.product.service.file.impl;

import com.danghieu99.monolith.product.service.file.ProductUploadFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductUploadFileToCloudService implements ProductUploadFileService {

    @Override
    public void uploadImage(String fileName, MultipartFile file) {

    }
}
