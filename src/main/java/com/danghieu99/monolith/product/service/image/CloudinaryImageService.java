package com.danghieu99.monolith.product.service.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

@Service("product-cloudinary-image-service")
@Slf4j
public class CloudinaryImageService implements ImageService {

    private final Cloudinary cloudinary;

    public CloudinaryImageService(@Qualifier("product-cloudinary") Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public void upload(@NotBlank final String token, @NotNull MultipartFile file) throws IOException {
        try {
            cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", token,
                    "eager", Collections.singletonList(new Transformation().width(1920).height(1080).crop("limit").quality("auto").fetchFormat("auto"))
                    , "resource_type", "image"
            ));
        } catch (IOException e) {
            log.error("Cloudinary upload error: {} for file: {}", e.getMessage(), file.getName());
            throw e;
        }
        log.info("Cloudinary upload for file: {} success", file.getName());
    }
}