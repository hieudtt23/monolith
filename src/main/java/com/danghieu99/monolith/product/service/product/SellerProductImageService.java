package com.danghieu99.monolith.product.service.product;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.product.dto.response.SaveImagesResponse;
import com.danghieu99.monolith.product.dto.response.SaveVariantImageResponse;
import com.danghieu99.monolith.product.entity.jpa.Image;
import com.danghieu99.monolith.product.entity.jpa.join.ProductImage;
import com.danghieu99.monolith.product.entity.jpa.join.VariantImage;
import com.danghieu99.monolith.product.repository.jpa.ImageRepository;
import com.danghieu99.monolith.product.repository.jpa.VariantRepository;
import com.danghieu99.monolith.product.repository.jpa.join.ProductImageRepository;
import com.danghieu99.monolith.product.repository.jpa.join.VariantImageRepository;
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
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Slf4j
public class SellerProductImageService {

    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final ProductImageRepository productImageRepository;
    private final VariantRepository variantRepository;
    private final VariantImageRepository variantImageRepository;

    public SellerProductImageService(@Qualifier("product-cloudinary-image-service") ImageService imageService,
                                     ImageRepository imageRepository,
                                     ProductImageRepository productImageRepository,
                                     VariantRepository variantRepository, VariantImageRepository variantImageRepository) {
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.productImageRepository = productImageRepository;
        this.variantRepository = variantRepository;
        this.variantImageRepository = variantImageRepository;
    }

    @Transactional
    public SaveImagesResponse uploadAndSave(@NotBlank final String productUUID,
                                            @NotEmpty @Size(max = 10) final List<@NotNull MultipartFile> imgFiles) {
        Collection<Image> images = new ConcurrentLinkedQueue<>();
        Collection<String> failedFileNames = new ConcurrentLinkedQueue<>();
        List<ProductImage> productImages = new ArrayList<>();

        imgFiles.parallelStream().forEach(file -> {
            try {
                String token = "product-image_" + UUID.randomUUID();
                var upload = imageService.upload(token, file.getBytes());
                Image image = Image.builder()
                        .token(token)
                        .build();
                ProductImage productImage = ProductImage.builder()
                        .imageToken(token)
                        .productUUID(UUID.fromString(productUUID))
                        .build();
                upload.join();
                if (upload.isCompletedExceptionally()) {
                    failedFileNames.add(file.getName());
                } else {
                    images.add(image);
                    productImages.add(productImage);
                }
            } catch (IOException e) {
                log.error("ProductUUID: {}, file: {} upload failed", productUUID, file.getName(), e);
            }
        });
        SaveImagesResponse response = new SaveImagesResponse();
        if (!images.isEmpty() && !productImages.isEmpty()) {
            imageRepository.saveAll(images);
            productImageRepository.saveAll(productImages);
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
        }
        if (!failedFileNames.isEmpty()) {
            response.setMessage("Upload failed for files: " + failedFileNames);
        }
        return response;
    }

    @Transactional
    public SaveVariantImageResponse saveVariantImage(@NotEmpty Map<@NotBlank String, @NotBlank String> variantImageMap) {
        Map<String, String> failedMap = new HashMap<>();
        List<VariantImage> variantImages = new ArrayList<>();
        variantImageMap.forEach((variantUUID, imageToken) -> {
            try {
                int variantId = variantRepository.findByUuid(UUID.fromString(variantUUID))
                        .orElseThrow(() -> new ResourceNotFoundException("Variant", "uuid", variantUUID))
                        .getId();
                VariantImage variantImage = VariantImage.builder()
                        .variantId(variantId)
                        .imageToken(imageToken)
                        .build();
                variantImages.add(variantImage);
            } catch (ResourceNotFoundException e) {
                log.error("VariantUUID: {}, imageToken: {} upload failed", variantUUID, imageToken);
                failedMap.put(variantUUID, imageToken);
            }
        });
        if (!variantImages.isEmpty()) {
            variantImageRepository.saveAll(variantImages);
        }
        return SaveVariantImageResponse.builder()
                .failedMap(failedMap)
                .build();
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
