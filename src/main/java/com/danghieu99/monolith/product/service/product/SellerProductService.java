package com.danghieu99.monolith.product.service.product;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.product.dto.request.SaveProductRequest;
import com.danghieu99.monolith.product.dto.request.SaveVariantRequest;
import com.danghieu99.monolith.product.dto.request.UpdateProductDetailsRequest;
import com.danghieu99.monolith.product.dto.response.VariantDetailsResponse;
import com.danghieu99.monolith.product.dto.response.ProductDetailsResponse;
import com.danghieu99.monolith.product.entity.*;
import com.danghieu99.monolith.product.entity.join.*;
import com.danghieu99.monolith.product.mapper.ProductMapper;
import com.danghieu99.monolith.product.mapper.VariantMapper;
import com.danghieu99.monolith.product.repository.jpa.*;
import com.danghieu99.monolith.product.repository.jpa.join.*;
import com.danghieu99.monolith.product.service.image.CloudinaryImageService;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerProductService {

    private final ProductMapper productMapper;
    private final VariantMapper variantMapper;
    private final ProductCategoryRepository productCategoryRepository;
    private final VariantAttributeRepository variantAttributeRepository;
    private final ProductShopRepository productShopRepository;
    private final VariantRepository variantRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    private final ImageRepository imageRepository;
    private final ProductImageRepository productImageRepository;
    private final VariantImageRepository variantImageRepository;
    private final CloudinaryImageService cloudinaryImageService;

    public Page<ProductDetailsResponse> getAllByCurrentShop(@NotNull UserDetailsImpl userDetails,
                                                            @NotNull Pageable pageable) {
        Page<Product> products = productRepository.findByShopUUID(UUID.fromString(userDetails.getUuid()), pageable);
        return products.map(productMapper::toGetProductDetailsResponse);
    }

    @Transactional
    public void saveProductToCurrentShop(@NotNull UserDetailsImpl userDetails,
                                         @NotNull SaveProductRequest request) {
        Set<ProductCategory> productCategories = new HashSet<>();

        Product newProduct = productMapper.toProduct(request);
        var savedProduct = productRepository.saveAndFlush(newProduct);
        var imageUploads = this.uploadImages(request.getImages());
        Shop shop = shopRepository.findByAccountUUID(userDetails.getUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Shop", "accountId", userDetails.getUuid()));
        ProductShop productShop = ProductShop.builder()
                .productId(savedProduct.getId())
                .shopId(shop.getId())
                .build();
        productShopRepository.saveAndFlush(productShop);
        request.getCategories()
                .forEach(category -> productCategories.add(ProductCategory.builder()
                        .categoryId(categoryRepository.findByName(category.trim())
                                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", category.trim()))
                                .getId())
                        .productId(savedProduct.getId())
                        .build()));
        productCategoryRepository.saveAllAndFlush(productCategories);
        this.saveVariantsByProductId(savedProduct.getId(), request.getVariants());

        List<Image> images = imageUploads.join();
        List<Image> savedImages = imageRepository.saveAllAndFlush(images);
        Set<ProductImage> productImages = new HashSet<>(savedImages.stream().map(image -> ProductImage.builder()
                        .productUUID(savedProduct.getUuid())
                        .imageUUID(image.getUuid())
                        .build())
                .toList());
        productImageRepository.saveAllAndFlush(productImages);
    }

    @Transactional
    public void updateProductDetailsByUUID(@NotBlank String uuid,
                                           @NotNull UpdateProductDetailsRequest request) {
        var product = productRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new ResourceNotFoundException("Product", "uuid", uuid));
        if (request.getName() != null && !request.getName().isBlank()) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            product.setDescription(request.getDescription());
        }
        productRepository.save(product);
    }

    @Transactional
    public void deleteProductByUUID(@NotBlank String uuid) {
        productRepository.deleteByUuid(UUID.fromString(uuid));
        productCategoryRepository.deleteByProductUUID(UUID.fromString(uuid));
        productShopRepository.deleteByProductUUID(UUID.fromString(uuid));
        variantRepository.deleteByProductUUID(UUID.fromString(uuid));
    }

    @Transactional
    public Page<VariantDetailsResponse> getVariantsByProductUUID(@NotBlank String productUUID, @NotNull Pageable
            pageable) {
        return variantRepository.findByProductUuid(UUID.fromString(productUUID), pageable).map(variantMapper::toResponse);
    }

    @Transactional
    public void updateVariantPriceStockByUUID(@NotBlank String variantUUID, @NotNull SaveVariantRequest request) {
        var current = variantRepository.findByUuid(UUID.fromString(variantUUID))
                .orElseThrow(() -> new ResourceNotFoundException("Variant", "uuid", variantUUID));
        if (request.getPrice() != null) {
            current.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            current.setStock(request.getStock());
        }
        variantRepository.save(current);
    }

    @Transactional
    public void saveVariantsByProductId(@NotNull int productId, List<SaveVariantRequest> request) {
        List<VariantAttribute> variantAttributes = new ArrayList<>();
        List<VariantImage> variantImages = new ArrayList<>();
        request.forEach(requestVariant -> {
            var imageUploads = this.uploadImages(requestVariant.getImages());
            var variant = variantMapper.toVariant(requestVariant);
            variant.setProductId(productId);
            variant.setPrice(requestVariant.getPrice());
            variant.setStock(requestVariant.getStock());
            Variant savedVariant = variantRepository.saveAndFlush(variant);
            requestVariant.getAttributes().forEach((key, value) -> {
                var savedAttribute = attributeRepository.saveAndFlush(Attribute.builder()
                        .type(key)
                        .value(value)
                        .build());
                variantAttributes.add(VariantAttribute.builder()
                        .variantId(savedVariant.getId())
                        .attributeId(savedAttribute.getId())
                        .attributeType(savedAttribute.getType())
                        .build());
                List<Image> images = imageUploads.join();
                List<Image> savedImages = imageRepository.saveAllAndFlush(images);
                variantImages.addAll(savedImages.stream().map(image -> VariantImage.builder()
                                .variantId(savedVariant.getId())
                                .imageUUID(image.getUuid())
                                .build())
                        .toList());
            });
        });
        variantAttributeRepository.saveAll(variantAttributes);
        variantImageRepository.saveAll(variantImages);
    }

    @Transactional
    public void deleteVariant(@NotBlank String variantUUID) {
        variantRepository.deleteByUuid(UUID.fromString(variantUUID));
        variantAttributeRepository.deleteByVariantUUID(UUID.fromString(variantUUID));
    }

    @Transactional
    public void deleteAttributeByProductUUIDTypeValue(String productUUID, String type, String value) {
        var attribute = attributeRepository
                .findByProductUUIDAttributeTypeValueContainsIgnoreCase(UUID.fromString(productUUID), type, value)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute", "productUUID", productUUID));
        attributeRepository.deleteByUuid(attribute.getUuid());
        variantAttributeRepository.deleteByProductUUID(UUID.fromString(productUUID));
    }

    @Transactional
    public void deleteAttributeByUUID(@NotBlank String attributeUUID) {
        attributeRepository.deleteByUuid(UUID.fromString(attributeUUID));
        variantAttributeRepository.deleteByAttributeUUID(UUID.fromString(attributeUUID));
    }

    @Async
    @Transactional
    protected CompletableFuture<List<Image>> uploadImages(@NotEmpty List<MultipartFile> files) {
        List<Image> images = new ArrayList<>();
        files.forEach(file -> {
            try {
                String url = cloudinaryImageService.uploadImage(file);
                log.info("Image: {} uploaded successfully", file.getName());
                images.add(Image.builder()
                        .url(url)
                        .build());
            } catch (IOException e) {
                log.error("Error uploading image", e);
            }
        });
        return CompletableFuture.completedFuture(images);
    }
}