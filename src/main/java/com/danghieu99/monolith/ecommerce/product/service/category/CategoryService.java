package com.danghieu99.monolith.ecommerce.product.service.category;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.ecommerce.product.dto.response.CategoryResponse;
import com.danghieu99.monolith.ecommerce.product.mapper.CategoryMapper;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.CategoryRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public Page<CategoryResponse> getAll(@NotNull final Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toResponse);
    }

    public CategoryResponse getByUUID(@NotNull final String uuid) {
        return categoryMapper.toResponse(categoryRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new ResourceNotFoundException("Category", "uuid", uuid)));
    }

    public Page<CategoryResponse> getBySuperCategoryUUID(@NotNull final String uuid, @NotNull final Pageable pageable) {
        return categoryRepository.findBySuperCategoryUUID(UUID.fromString(uuid), pageable).map(categoryMapper::toResponse);
    }

    public Page<CategoryResponse> getBySubCategoryUUID(@NotNull final String uuid, @NotNull final Pageable pageable) {
        return categoryRepository.findBySubCategoryUUID(UUID.fromString(uuid), pageable).map(categoryMapper::toResponse);
    }

    public Page<CategoryResponse> getByNameContaining(@NotNull final String name, @NotNull final Pageable pageable) {
        return categoryRepository.findByNameContaining(name, pageable).map(categoryMapper::toResponse);
    }
}
