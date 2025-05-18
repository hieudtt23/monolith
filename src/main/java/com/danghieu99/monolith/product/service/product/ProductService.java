package com.danghieu99.monolith.product.service.product;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.product.dto.response.ProductDetailsResponse;
import com.danghieu99.monolith.product.entity.Product;
import com.danghieu99.monolith.product.mapper.ProductMapper;
import com.danghieu99.monolith.product.repository.jpa.CategoryRepository;
import com.danghieu99.monolith.product.repository.jpa.ProductRepository;
import jakarta.validation.constraints.NotBlank;
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
    private final CategoryRepository categoryRepository;

    public Page<Product> getAll(@NotNull Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getByCategoryId(@NotNull int categoryId, @NotNull Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public Page<Product> getByCategoryUUID(@NotBlank String categoryUUID, @NotNull Pageable pageable) {
        return productRepository.findByCategoryUUID(UUID.fromString(categoryUUID), pageable);
    }

    public Page<Product> getByCategoryUUIDsAny(@NotNull List<String> categoryUUIDs, @NotNull Pageable pageable) {
        List<UUID> uuids = categoryUUIDs.stream().map(UUID::fromString).toList();
        return productRepository.findByCategoryUUIDsAny(uuids, pageable);
    }

    public Page<Product> getByShopUUID(@NotNull String shopUUID, @NotNull Pageable pageable) {
        return productRepository.findByShopUUID(UUID.fromString(shopUUID), pageable);
    }

    public ProductDetailsResponse getProductDetailsByUUID(@NotBlank String uuid) {
        var product = productRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new ResourceNotFoundException("Product", "uuid", uuid));
        var response = productMapper.toGetProductDetailsResponse(product);
        response.setCategories(categoryRepository.findByProductUUID(product.getUuid()));
        return response;
    }
}