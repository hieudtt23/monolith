package com.danghieu99.monolith.ecommerce.product.service.product;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.ecommerce.product.constant.EImageRole;
import com.danghieu99.monolith.ecommerce.product.dto.response.GetProductDetailsResponse;
import com.danghieu99.monolith.ecommerce.product.dto.response.GetProductResponse;
import com.danghieu99.monolith.ecommerce.product.dto.response.GetReviewResponse;
import com.danghieu99.monolith.ecommerce.product.dto.response.GetVariantDetailsResponse;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Category;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Image;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Product;
import com.danghieu99.monolith.ecommerce.product.mapper.ProductMapper;
import com.danghieu99.monolith.ecommerce.product.mapper.VariantMapper;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.*;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.join.VariantImageRepository;
import com.danghieu99.monolith.ecommerce.product.repository.redis.RecentViewRepository;
import com.danghieu99.monolith.ecommerce.product.service.review.ReviewService;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final RecentViewService recentViewService;
    private final RecentViewRepository recentViewRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final VariantRepository variantRepository;
    private final ImageRepository imageRepository;
    private final VariantImageRepository variantImageRepository;
    private final AttributeRepository attributeRepository;
    private final ProductMapper productMapper;
    private final VariantMapper variantMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    public Page<GetProductResponse> getAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::getProductResponseFromProduct);
    }

    public Page<GetProductResponse> getByCategoryId(@NotBlank int id,
                                                    @NotNull Pageable pageable) {
        return productRepository.findByCategoryId(id, pageable).map(this::getProductResponseFromProduct);
    }

    public Page<GetProductResponse> getByCategoryUUID(@NotBlank String categoryUUID,
                                                      @NotNull Pageable pageable) {
        return productRepository.findByCategoryUUID(UUID.fromString(categoryUUID), pageable).map(this::getProductResponseFromProduct);
    }

    public Page<GetProductResponse> getByCategoryUUIDsAny(@NotEmpty List<@NotBlank String> categoryUUIDs,
                                                          @NotNull Pageable pageable) {
        List<UUID> uuids = categoryUUIDs.stream().map(UUID::fromString).toList();
        return productRepository.findByCategoryUUIDsAny(uuids, pageable).map(this::getProductResponseFromProduct);
    }

    public Page<GetProductResponse> getByShopUUID(@NotBlank String shopUUID,
                                                  @NotNull Pageable pageable) {
        var products = productRepository.findByShopUUID(UUID.fromString(shopUUID), pageable);
        return products.map(this::getProductResponseFromProduct);
    }

    public GetProductDetailsResponse getProductDetailsByUUID(@NotBlank String uuid) {
        var product = productRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new ResourceNotFoundException("Product", "uuid", uuid));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //separate to aop aspect
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            recentViewService.saveRecentlyViewed(userDetails.getUuid(), uuid);
        }
        return this.getProductDetailsResponseFromProduct(product);
    }

    public Page<GetProductResponse> getRecentlyViewedByAccountUUID(@NotBlank String accountUUID, @NotNull Pageable pageable) {
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

    protected GetProductResponse getProductResponseFromProduct(@NotNull Product product) {
        String imgToken = "";
        var image = imageRepository.findByProductUUIDAndRole(product.getUuid(), EImageRole.FRONT);
        if (image.isPresent()) {
            imgToken = image.get().getToken();
        }
        return GetProductResponse.builder()
                .uuid(product.getUuid().toString())
                .name(product.getName())
                .imageToken(imgToken)
                .status(product.getStatus())
                .build();
    }

    protected GetProductDetailsResponse getProductDetailsResponseFromProduct(@NotNull Product product) {
        var response = productMapper.toGetProductDetailsResponse(product);
        response.setUuid(product.getUuid().toString());
        response.setShopUUID(shopRepository.findByProductUuid(product.getUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Shop", "productUUID", product.getUuid()))
                .getUuid().toString());
        response.setCategories(categoryRepository.findByProductUUID(product.getUuid()).stream().map(Category::getName).toList());
        response.setVariants(variantRepository.findByProductId(product.getId()).parallelStream().map(variant -> {
            GetVariantDetailsResponse variantResponse = variantMapper.toResponse(variant);
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
        Page<GetReviewResponse> reviews = reviewService.getReviewsByProductUUID(product.getUuid().toString(), Pageable.ofSize(5));
        response.setReviews(reviews);
        return response;
    }
}