package com.danghieu99.monolith.ecommerce.product.service.product;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.ecommerce.product.dto.request.SaveVariantImageRequest;
import com.danghieu99.monolith.ecommerce.product.dto.response.PostImagesResponse;
import com.danghieu99.monolith.ecommerce.product.dto.response.PostVariantImageResponse;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Image;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Product;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Variant;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.join.ProductImage;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.join.VariantImage;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.ImageRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.ProductRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.VariantRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.join.ProductImageRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.join.VariantImageRepository;
import com.danghieu99.monolith.ecommerce.product.service.image.ImageService;
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
    private final ProductRepository productRepository;

    public SellerProductImageService(@Qualifier("product-cloudinary-image-service") ImageService imageService,
                                     ImageRepository imageRepository,
                                     ProductImageRepository productImageRepository,
                                     VariantRepository variantRepository,
                                     VariantImageRepository variantImageRepository,
                                     ProductRepository productRepository) {
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.productImageRepository = productImageRepository;
        this.variantRepository = variantRepository;
        this.variantImageRepository = variantImageRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public PostImagesResponse uploadAndSaveProductImages(@NotBlank final String productUUID,
                                                         @Size(min = 1, max = 10) final List<@NotNull MultipartFile> imgFiles) {
        Product product = productRepository.findByUuid(UUID.fromString(productUUID))
                .orElseThrow(() -> new ResourceNotFoundException("Product", "uuid", productUUID));

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
                        .productId(product.getId())
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
        PostImagesResponse response = new PostImagesResponse();
        if (!images.isEmpty() && !productImages.isEmpty()) {
            imageRepository.saveAll(images);
            productImageRepository.saveAll(productImages);
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
        }
        if (!failedFileNames.isEmpty()) {
            response.setFailedFileNames(failedFileNames.toArray(String[]::new));
        }
        return response;
    }

    @Transactional
    public void replaceProductImage(@NotBlank final String imageToken,
                                    @NotNull final MultipartFile file) {
        try {
            if (!productImageRepository.existsByImageToken(imageToken)) {
                throw new ResourceNotFoundException("ProductImage", "imageToken", imageToken);
            }
            imageService.upload(imageToken, file.getBytes());
        } catch (Exception e) {
            log.error("imageToken: {}, fileName: {} replace failed. Exception: {}", imageToken, file.getName(), e.toString());
        }
    }

    @Transactional
    public void deleteProductImagesByProductUUID(@NotBlank final String productUUID) {
        Collection<ProductImage> productImages = productImageRepository.findByProductUUID(UUID.fromString(productUUID));
        Collection<String> tokens = productImages.stream().map(ProductImage::getImageToken).toList();
        tokens.parallelStream().forEach(this::deleteProductImageByToken);
    }

    @Transactional
    public void deleteProductImageByToken(@NotBlank final String token) {
        try {
            var delete = imageService.deleteImage(token);
            delete.join();
            if (!delete.isCompletedExceptionally()) {
                imageRepository.deleteByToken(token);
                productImageRepository.deleteByImageToken(token);
            }
        } catch (Exception e) {
            log.error("imageToken {} delete failed", token);
        }
    }

    @Transactional
    public PostVariantImageResponse saveVariantImage(@NotEmpty final List<@NotNull SaveVariantImageRequest> requests) {
        Map<String, String> failedMap = new HashMap<>();
        List<VariantImage> variantImages = new ArrayList<>();
        requests.forEach((request) -> {
            try {
                Variant variant = variantRepository.findByUuid(UUID.fromString(request.getVariantUUID()))
                        .orElseThrow(() -> new ResourceNotFoundException("Variant", "uuid", request.getVariantUUID()));
                ProductImage productImage = productImageRepository.findByImageToken(request.getImageToken())
                        .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "imageToken", request.getImageToken()));
                if (variant.getProductId() != productImage.getProductId()) {
                    throw new IllegalArgumentException("Invalid product image");
                }
                VariantImage variantImage = VariantImage.builder()
                        .variantId(variant.getId())
                        .imageToken(request.getImageToken())
                        .build();
                variantImages.add(variantImage);
            } catch (ResourceNotFoundException e) {
                log.error("Save variantUUID: {}, imageToken: {}, Exception: {}", request.getVariantUUID(), request.getImageToken(), e.toString());
                failedMap.put(request.getVariantUUID(), request.getImageToken());
                throw e;
            }
        });
        if (!variantImages.isEmpty()) {
            variantImageRepository.saveAll(variantImages);
        }
        return PostVariantImageResponse.builder()
                .failedMap(failedMap)
                .build();
    }

    @Transactional
    public void deleteByProductUUID(final String productUUID) {
        UUID uuid = UUID.fromString(productUUID);
        imageRepository.deleteByProductUUID(uuid);
        productImageRepository.deleteByProductUUID(uuid);
    }

    @Transactional
    public void deleteByImageToken(final String token) {
        productImageRepository.deleteByImageToken(token);
        imageRepository.deleteByToken(token);
    }

    @Transactional
    public void setImageRole(final String token, final String role) {
        productImageRepository.updateRoleByImageToken(token, role);
    }
}