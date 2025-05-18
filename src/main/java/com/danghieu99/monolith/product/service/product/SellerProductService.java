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
import com.danghieu99.monolith.product.service.file.ProductUploadFileService;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerProductService {

    private final ProductMapper productMapper;
    private final VariantMapper variantMapper;
    private final ProductCategoryRepository pCategoryRepository;
    private final VariantAttributeRepository vAttributeRepository;
    private final ProductShopRepository productShopRepository;
    private final VariantAttributeRepository variantAttributeRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final VariantRepository variantRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    private final ProductUploadFileService orderUploadFileService;
    private final ImageRepository imageRepository;
    private final ProductImageRepository productImageRepository;
    private final VariantImageRepository variantImageRepository;

    public Page<ProductDetailsResponse> getAllByCurrentShop(@NotNull UserDetailsImpl userDetails,
                                                            @NotNull Pageable pageable) {
        Page<Product> products = productRepository.findByShopUUID(UUID.fromString(userDetails.getUuid()), pageable);
        return products.map(productMapper::toGetProductDetailsResponse);
    }

    @Transactional
    public void addToCurrentShop(@NotNull UserDetailsImpl userDetails,
                                 @NotNull SaveProductRequest request) {
        ProductShop productShop;
        Set<ProductCategory> productCategories = new HashSet<>();
        Set<Variant> variants = new HashSet<>();
        Set<VariantAttribute> variantAttributes = new HashSet<>();
        Set<ProductImage> productImages = new HashSet<>();
        Set<VariantImage> variantImages = new HashSet<>();

        Product newProduct = productMapper.toProduct(request);
        var savedProduct = productRepository.saveAndFlush(newProduct);
        productShop = ProductShop.builder()
                .productId(savedProduct.getId())
                .shopId(shopRepository.findByAccountUUID(userDetails.getUuid())
                        .orElseThrow(() -> new ResourceNotFoundException("Shop", "accountId", userDetails.getUuid()))
                        .getId())
                .build();

        request.getCategories()
                .forEach(category -> productCategories.add(ProductCategory.builder()
                        .categoryId(categoryRepository.findByName(category.trim())
                                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", category.trim()))
                                .getId())
                        .productId(savedProduct.getId())
                        .build()));

        request.getImgFiles().forEach(requestProductImage -> {
            String productImgObjectName = savedProduct.getUuid() + "/full/" + requestProductImage.getOriginalFilename();
            try {
                orderUploadFileService.uploadImage(productImgObjectName, requestProductImage);
            } catch (Exception e) {
                throw new RuntimeException("Cannot upload image: " + requestProductImage.getOriginalFilename(), e);
            }
            Image image = imageRepository.saveAndFlush(Image.builder()
                    .objectName(productImgObjectName)
                    .build());
            productImages.add(ProductImage.builder()
                    .imageId(image.getId())
                    .productId(savedProduct.getId())
                    .build());
        });

        request.getVariants().forEach(requestVariant -> {
            var variant = variantMapper.toVariant(requestVariant);
            variant.setProductId(savedProduct.getId());
            variant.setPrice(requestVariant.getPrice());
            variant.setStock(requestVariant.getStock());
            var savedVariant = variantRepository.saveAndFlush(variant);
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

                String variantImgObjectName = savedVariant.getUuid() + "/full/" + requestVariant.getImgFile().getOriginalFilename();
                try {
                    orderUploadFileService.uploadImage(variantImgObjectName, requestVariant.getImgFile());
                } catch (Exception e) {
                    throw new RuntimeException("Cannot upload image: " + requestVariant.getImgFile().getOriginalFilename(), e);
                }
                Image image = imageRepository.saveAndFlush(Image.builder()
                        .objectName(variantImgObjectName)
                        .build());
                variantImages.add(VariantImage.builder()
                        .imageId(image.getId())
                        .variantId(savedVariant.getId())
                        .build());
            });
        });

        productShopRepository.save(productShop);
        variantRepository.saveAll(variants);
        pCategoryRepository.saveAll(productCategories);
        vAttributeRepository.saveAll(variantAttributes);
        productImageRepository.saveAll(productImages);
        variantImageRepository.saveAll(variantImages);
    }

    @Transactional
    public void updateProductDetailsByUUID(@NotBlank String uuid, @NotNull UpdateProductDetailsRequest request) {
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
    public void saveVariant(@NotNull SaveVariantRequest request) {
        Product product = productRepository.findByUuid(UUID.fromString(request.getProductUUID()))
                .orElseThrow(() -> new ResourceNotFoundException("Product", "uuid", request.getProductUUID()));

        Set<Integer> attributeIds = new HashSet<>();
        request.getAttributes().forEach((type, value) -> {
            Optional<Attribute> attribute = attributeRepository
                    .findByProductUUIDAttributeTypeValueContainsIgnoreCase(UUID.fromString(request.getProductUUID()), type, value);
            if (attribute.isPresent()) {
                attributeIds.add(attribute.get().getId());
            } else {
                Attribute newAttribute = Attribute.builder()
                        .productId(product.getId())
                        .type(type)
                        .value(value)
                        .build();
                attributeIds.add(attributeRepository.saveAndFlush(newAttribute).getId());
            }
        });

        Variant newVariant = variantMapper.toVariant(request);
        newVariant.setProductId(product.getId());
        newVariant.setStock(request.getStock());
        newVariant.setPrice(request.getPrice());
        Variant savedVariant = variantRepository.saveAndFlush(newVariant);

        Set<VariantAttribute> vAttributes = new HashSet<>();
        attributeIds.forEach(attributeId -> {
            vAttributes.add(VariantAttribute.builder()
                    .variantId(savedVariant.getId())
                    .attributeId(attributeId)
                    .build());
        });
        Set<VariantImage> vImages = new HashSet<>();
        String objectName = savedVariant.getUuid() + "/full/" + request.getImgFile().getOriginalFilename();
        try {
            orderUploadFileService.uploadImage(objectName, request.getImgFile());
        } catch (Exception e) {
            throw new RuntimeException("Cannot upload image: " + request.getImgFile().getOriginalFilename(), e);
        }

        Image image = Image.builder()
                .objectName(objectName)
                .build();
        imageRepository.save(image);
        variantAttributeRepository.saveAll(vAttributes);
        variantImageRepository.saveAll(vImages);
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
}