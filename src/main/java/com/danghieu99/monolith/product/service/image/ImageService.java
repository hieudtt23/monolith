package com.danghieu99.monolith.product.service.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    void upload(String token, MultipartFile file) throws IOException;
}
