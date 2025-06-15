package com.danghieu99.monolith.ecommerce.product.service.category;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.ecommerce.product.dto.request.SaveCategoryRequest;
import com.danghieu99.monolith.ecommerce.product.dto.response.CategoryResponse;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Category;
import com.danghieu99.monolith.ecommerce.product.mapper.CategoryMapper;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.CategoryRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.join.ProductCategoryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductCategoryRepository productCategoryRepository;

    @Transactional
    public Category save(@NotNull final SaveCategoryRequest request) {
        return categoryRepository.save(categoryMapper.toCategory(request));
    }

    @Transactional
    public void deleteById(@NotNull int id) {
        categoryRepository.deleteById(id);
        productCategoryRepository.deleteByCategoryId(id);
    }

    public CategoryResponse getById(@NotNull int id) {
        return categoryMapper.toResponse(categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id)));
    }

    @Transactional
    public CategoryResponse updateById(@NotNull final int id, @NotNull final SaveCategoryRequest request) {
        Category current = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        if (request.getName() != null) {
            current.setName(request.getName());
        }
        if (request.getDescription() != null) {
            current.setDescription(request.getDescription());
        }
        return categoryMapper.toResponse(categoryRepository.save(current));
    }
}
