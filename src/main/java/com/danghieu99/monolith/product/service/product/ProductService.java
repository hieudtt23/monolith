package com.danghieu99.monolith.product.service.product;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.product.constant.EImageRole;
import com.danghieu99.monolith.product.dto.response.ProductDetailsResponse;
import com.danghieu99.monolith.product.dto.response.ProductResponse;
import com.danghieu99.monolith.product.entity.jpa.Category;
import com.danghieu99.monolith.product.entity.jpa.Image;
import com.danghieu99.monolith.product.entity.jpa.Product;
import com.danghieu99.monolith.product.mapper.ProductMapper;
import com.danghieu99.monolith.product.mapper.VariantMapper;
import com.danghieu99.monolith.product.repository.jpa.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final VariantRepository variantRepository;
    private final VariantMapper variantMapper;
    private final ImageRepository imageRepository;

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

    protected ProductDetailsResponse getProductDetailsResponseFromProduct(@NotNull Product product) {
        var response = productMapper.toGetProductDetailsResponse(product);
        response.setShopUUID(shopRepository.findByProductUuid(product.getUuid()).getUuid().toString());
        response.setCategories(categoryRepository.findByProductUUID(product.getUuid()).stream().map(Category::getName).toList());
        response.setVariants(variantRepository.findByProductId(product.getId()).stream().map(variantMapper::toResponse).toList());
        response.setImageToken(imageRepository.findByProductUUID(product.getUuid()).stream().map(Image::getToken).toList());
        return response;
    }

    protected ProductResponse getProductResponseFromProduct(@NotNull Product product) {
        var response = productMapper.toProductResponse(product);
        response.setImageToken(imageRepository.findByProductUUIDAndRole(product.getUuid(), EImageRole.FRONT)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "uuid", product.getUuid()))
                .getToken());
        return response;
    }
}