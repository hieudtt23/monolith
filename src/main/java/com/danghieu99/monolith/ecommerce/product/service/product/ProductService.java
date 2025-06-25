package com.danghieu99.monolith.ecommerce.product.service.product;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.ecommerce.product.constant.EImageRole;
import com.danghieu99.monolith.ecommerce.product.dto.response.ProductDetailsResponse;
import com.danghieu99.monolith.ecommerce.product.dto.response.ProductResponse;
import com.danghieu99.monolith.ecommerce.product.dto.response.VariantDetailsResponse;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Category;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Image;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Product;
import com.danghieu99.monolith.ecommerce.product.entity.redis.RecentView;
import com.danghieu99.monolith.ecommerce.product.mapper.ProductMapper;
import com.danghieu99.monolith.ecommerce.product.mapper.VariantMapper;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.*;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.join.VariantImageRepository;
import com.danghieu99.monolith.ecommerce.product.repository.redis.RecentViewRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final VariantRepository variantRepository;
    private final ImageRepository imageRepository;
    private final VariantImageRepository variantImageRepository;
    private final AttributeRepository attributeRepository;
    private final RecentViewRepository recentViewRepository;
    private final ProductMapper productMapper;
    private final VariantMapper variantMapper;

    public Page<ProductResponse> getAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::getProductResponseFromProduct);
    }

    public Page<ProductResponse> getByCategoryId(@NotBlank int id,
                                                 @NotNull Pageable pageable) {
        return productRepository.findByCategoryId(id, pageable).map(this::getProductResponseFromProduct);
    }

    public Page<ProductResponse> getByCategoryUUID(@NotBlank String categoryUUID,
                                                   @NotNull Pageable pageable) {
        return productRepository.findByCategoryUUID(UUID.fromString(categoryUUID), pageable).map(this::getProductResponseFromProduct);
    }

    public Page<ProductResponse> getByCategoryUUIDsAny(@NotEmpty List<@NotBlank String> categoryUUIDs,
                                                       @NotNull Pageable pageable) {
        List<UUID> uuids = categoryUUIDs.stream().map(UUID::fromString).toList();
        return productRepository.findByCategoryUUIDsAny(uuids, pageable).map(this::getProductResponseFromProduct);
    }

    public Page<ProductResponse> getByShopUUID(@NotBlank String shopUUID,
                                               @NotNull Pageable pageable) {
        var products = productRepository.findByShopUUID(UUID.fromString(shopUUID), pageable);
        return products.map(this::getProductResponseFromProduct);
    }

    public ProductDetailsResponse getProductDetailsByUUID(@NotBlank String uuid) {
        var product = productRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new ResourceNotFoundException("Product", "uuid", uuid));
        return this.getProductDetailsResponseFromProduct(product);
    }

    //use aop
    @Transactional
    protected void saveRecentlyViewed(@NotBlank String accountUUID, @NotBlank String productUUID) {
        var recentlyViewed = RecentView.builder()
                .accountUUID(accountUUID)
                .productUUID(productUUID)
                .timestamp(Instant.now().toEpochMilli())
                .build();
        recentViewRepository.save(recentlyViewed);
    }

    @PreAuthorize("isAuthenticated()")
    public Page<ProductResponse> getRecentlyViewedByAccountUUID(@NotBlank String accountUUID, @NotNull Pageable pageable) {
        return recentViewRepository.findByAccountUUID(accountUUID, pageable).map(recentView -> {
            try {
                Product product = productRepository.findByUuid(UUID.fromString(recentView.getProductUUID()))
                        .orElseThrow(() -> new ResourceNotFoundException("Product", "uuid", recentView.getProductUUID()));
                return this.getProductResponseFromProduct(product);
            } catch (Exception e) {
                log.error("Find product failed, exception: {}", e.getMessage());
                return null;
            }
        });
    }

    protected ProductResponse getProductResponseFromProduct(@NotNull Product product) {
        String imgToken = "";
        var image = imageRepository.findByProductUUIDAndRole(product.getUuid(), EImageRole.FRONT);
        if (image.isPresent()) {
            imgToken = image.get().getToken();
        }
        return ProductResponse.builder()
                .uuid(product.getUuid().toString())
                .name(product.getName())
                .imageToken(imgToken)
                .status(product.getStatus())
                .build();
    }

    protected ProductDetailsResponse getProductDetailsResponseFromProduct(@NotNull Product product) {
        var response = productMapper.toGetProductDetailsResponse(product);
        response.setUuid(product.getUuid().toString());
        response.setShopUUID(shopRepository.findByProductUuid(product.getUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Shop", "productUUID", product.getUuid()))
                .getUuid().toString());
        response.setCategories(categoryRepository.findByProductUUID(product.getUuid()).stream().map(Category::getName).toList());
        response.setVariants(variantRepository.findByProductId(product.getId()).parallelStream().map(variant -> {
            VariantDetailsResponse variantResponse = variantMapper.toResponse(variant);
            Map<String, String> attributes = new HashMap<>();
            attributeRepository.findByVariantId(variant.getId())
                    .parallelStream().forEach(attribute -> {
                        attributes.put(attribute.getType(), attribute.getValue());
                    });
            variantResponse.setAttributes(attributes);
            var variantImage = variantImageRepository.findByVariantId(variant.getId());
            if (variantImage.isPresent()) {
                variantResponse.setImageToken(variantImageRepository.findByVariantId(variant.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("ImageToken", "id", variant.getId()))
                        .getImageToken());
            }
            return variantResponse;
        }).toList());
        var imageTokens = imageRepository.findByProductUUID(product.getUuid());
        if (!imageTokens.isEmpty()) {
            response.setImageTokens(imageTokens.stream().map(Image::getToken).toList());
        }
        return response;
    }
}