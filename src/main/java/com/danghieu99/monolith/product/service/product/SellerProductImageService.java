package com.danghieu99.monolith.product.service.product;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.danghieu99.monolith.product.dto.request.SaveProductImagesRequest;
import com.danghieu99.monolith.product.entity.Image;
import com.danghieu99.monolith.product.repository.jpa.ImageRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class SellerProductImageService {

    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;

    public SellerProductImageService(@Qualifier("product-cloudinary") Cloudinary cloudinary, ImageRepository imageRepository) {
        this.cloudinary = cloudinary;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public void save(@NotNull SaveProductImagesRequest request) {
        List<Image> images = new ArrayList<>();
        List<String> failedFileNames = new ArrayList<>();

        request.getImgFiles().forEach(file -> {
            try {
                String url = this.uploadImage(file);
                if (url != null && !url.isBlank()) {
                    log.info("ProductUUID: {} image: {} uploaded successfully", request.getProductUUID(), file.getName());
                    images.add(Image.builder()
                            .url(url)
                            .build());
                }
            } catch (IOException e) {
                log.error("ProductUUID: {} image: {} upload failed", request.getProductUUID(), file.getName(), e);
                failedFileNames.add(file.getName());
            }
        });
        if (!images.isEmpty()) {
            imageRepository.saveAll(images);
        } else {
            throw new RuntimeException("Upload images failed for product: " + request.getProductUUID() + " for files: " + failedFileNames);
        }
    }

    private String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "public_id", "product-image_" + UUID.randomUUID(),
                "eager", Collections.singletonList(new Transformation().width(1920).height(1080).crop("limit").quality("auto").fetchFormat("auto"))
                , "resource_type", "image"
        ));
        return (String) uploadResult.get("url");
    }
}
