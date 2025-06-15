package com.danghieu99.monolith.product.service.product;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.product.dto.request.SaveProductRequest;
import com.danghieu99.monolith.product.dto.request.SaveVariantRequest;
import com.danghieu99.monolith.product.dto.request.UpdateProductDetailsRequest;
import com.danghieu99.monolith.product.dto.response.VariantDetailsResponse;
import com.danghieu99.monolith.product.dto.response.ProductDetailsResponse;
import com.danghieu99.monolith.product.entity.jpa.Attribute;
import com.danghieu99.monolith.product.entity.jpa.Product;
import com.danghieu99.monolith.product.entity.jpa.Variant;
import com.danghieu99.monolith.product.entity.jpa.join.*;
import com.danghieu99.monolith.product.mapper.ProductMapper;
import com.danghieu99.monolith.product.mapper.VariantMapper;
import com.danghieu99.monolith.product.repository.jpa.*;
import com.danghieu99.monolith.product.repository.jpa.join.*;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

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
        var savedProduct = productRepository.save(newProduct);
        int shopId = shopRepository.findShopIdByAccountUUID(userDetails.getUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Shop id", "accountUUID", userDetails.getUuid()));
        ProductShop productShop = ProductShop.builder()
                .productId(savedProduct.getId())
                .shopId(shopId)
                .build();
        productShopRepository.save(productShop);
        request.getCategories()
                .forEach(category -> productCategories.add(ProductCategory.builder()
                        .categoryId(categoryRepository.findCategoryIdByName(category.trim())
                                .orElseThrow(() -> new ResourceNotFoundException("Category id", "name", category.trim())))
                        .productId(savedProduct.getId())
                        .build()));
        productCategoryRepository.saveAll(productCategories);
        this.saveVariantsByProductId(savedProduct.getId(), request.getVariants());
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
    public Page<VariantDetailsResponse> getVariantsByProductUUID(@NotBlank String productUUID,
                                                                 @NotNull Pageable pageable) {
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
    public void saveVariantsByProductId(@NotNull int productId,
                                        @NotEmpty List<@NotNull SaveVariantRequest> request) {
        List<VariantAttribute> variantAttributes = new ArrayList<>();
        List<Variant> variants = new ArrayList<>();
        List<Attribute> attributes = new ArrayList<>();

        request.forEach(requestVariant -> {
            var variant = variantMapper.toVariant(requestVariant);
            variant.setProductId(productId);
            variant.setPrice(requestVariant.getPrice());
            variant.setStock(requestVariant.getStock());
            variants.add(variant);
            requestVariant.getAttributes().forEach((key, value) -> {
                attributes.add(Attribute.builder()
                        .type(key)
                        .value(value)
                        .build());
            });
        });
        variantRepository.saveAll(variants);
        List<Attribute> savedAttributes = attributeRepository.saveAll(attributes);
        savedAttributes.forEach(attribute -> {
            variantAttributes.add(VariantAttribute.builder()
                    .variantId(attribute.getId())
                    .attributeId(attribute.getId())
                    .attributeType(attribute.getType())
                    .build());
        });
        variantAttributeRepository.saveAll(variantAttributes);
    }

    @Transactional
    public void deleteVariant(@NotBlank String variantUUID) {
        variantRepository.deleteByUuid(UUID.fromString(variantUUID));
        variantAttributeRepository.deleteByVariantUUID(UUID.fromString(variantUUID));
    }

    @Transactional
    public void deleteAttributeByProductUUIDTypeValue(String productUUID,
                                                      String type,
                                                      String value) {
        UUID uuid = UUID.fromString(productUUID);
        var attributeUUID = attributeRepository
                .findByProductUUIDAttributeTypeValueContainsIgnoreCase(uuid, type, value)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute", "productUUID", productUUID))
                .getUuid();
        attributeRepository.deleteByUuid(attributeUUID);
        variantAttributeRepository.deleteByAttributeUUID(attributeUUID);
    }

    @Transactional
    public void deleteAttributeByUUID(@NotBlank String attributeUUID) {
        attributeRepository.deleteByUuid(UUID.fromString(attributeUUID));
        variantAttributeRepository.deleteByAttributeUUID(UUID.fromString(attributeUUID));
    }
}