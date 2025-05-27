package com.danghieu99.monolith.product.service.product;

import com.danghieu99.monolith.product.entity.jpa.Image;
import com.danghieu99.monolith.product.entity.jpa.join.ProductImage;
import com.danghieu99.monolith.product.repository.jpa.ImageRepository;
import com.danghieu99.monolith.product.repository.jpa.join.ProductImageRepository;
import com.danghieu99.monolith.product.service.image.ImageService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class SellerProductImageService {

    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final ProductImageRepository productImageRepository;

    public SellerProductImageService(@Qualifier("product-cloudinary-image-service") ImageService imageService,
                                     ImageRepository imageRepository,
                                     ProductImageRepository productImageRepository) {
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.productImageRepository = productImageRepository;
    }

    @Transactional
    public void save(@NotBlank final String productUUID,
                     @NotEmpty @Size(max = 10) final List<@NotNull MultipartFile> imgFiles) {
        List<Image> images = new ArrayList<>();
        List<ProductImage> productImages = new ArrayList<>();
        imgFiles.forEach(file -> {
            try {
                String token = "product-image_" + UUID.randomUUID();
                imageService.upload(token, file);
                images.add(Image.builder()
                        .token(token)
                        .build());
                productImages.add(ProductImage.builder()
                        .imageToken(token)
                        .productUUID(UUID.fromString(productUUID))
                        .build());
            } catch (IOException e) {
                log.error("ProductUUID: {}, file: {} upload failed", productUUID, file.getName(), e);
            }
        });
        if (!images.isEmpty() && !productImages.isEmpty()) {
            imageRepository.saveAll(images);
            productImageRepository.saveAll(productImages);
        }
    }

    @Transactional
    public void deleteByProductUUID(String productUUID) {
        UUID uuid = UUID.fromString(productUUID);
        imageRepository.deleteByProductUUID(uuid);
        productImageRepository.deleteByProductUUID(uuid);
    }

    @Transactional
    public void deleteByImageToken(String token) {
        productImageRepository.deleteByImageToken(token);
        imageRepository.deleteByToken(token);
    }

    @Transactional
    public void setImageRole(String token, String role) {
        productImageRepository.updateRoleByImageToken(token, role);
    }
}
