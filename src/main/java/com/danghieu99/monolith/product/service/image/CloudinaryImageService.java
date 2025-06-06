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

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service("product-cloudinary-image-service")
@Slf4j
public class CloudinaryImageService implements ImageService {

    private final Cloudinary cloudinary;

    public CloudinaryImageService(@Qualifier("product-cloudinary") Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Async
    @Override
    public CompletableFuture<?> upload(@NotBlank final String token, @NotNull byte[] byteArray) {
        try {
            cloudinary.uploader().upload(byteArray, ObjectUtils.asMap(
                    "public_id", token,
                    "eager", Collections.singletonList(new Transformation()
                            .width(1920)
                            .height(1080)
                            .crop("limit")
                            .quality("auto")
                            .fetchFormat("auto"))
                    , "resource_type", "image"
            ));
            return CompletableFuture.completedFuture(null);
        } catch (IOException e) {
            log.error("Cloudinary upload error: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<?> uploadAndReturnUrl(String token, byte[] byteArray) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(byteArray, ObjectUtils.asMap(
                    "public_id", token,
                    "eager", Collections.singletonList(new Transformation()
                            .width(1920)
                            .height(1080)
                            .crop("limit")
                            .quality("auto")
                            .fetchFormat("auto"))
                    , "resource_type", "image"
            ));
            return CompletableFuture.completedFuture(result.get("url"));
        } catch (IOException e) {
            log.error("Cloudinary upload error: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }
}