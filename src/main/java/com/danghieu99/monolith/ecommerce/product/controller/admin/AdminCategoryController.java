package com.danghieu99.monolith.ecommerce.product.controller.admin;

import com.danghieu99.monolith.ecommerce.product.dto.request.SaveCategoryRequest;
import com.danghieu99.monolith.ecommerce.product.dto.response.CategoryResponse;
import com.danghieu99.monolith.ecommerce.product.mapper.CategoryMapper;
import com.danghieu99.monolith.ecommerce.product.service.category.AdminCategoryService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/category")
@RequiredArgsConstructor
@Validated
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping("")
    public CategoryResponse create(@NotNull @RequestBody SaveCategoryRequest category) {
        return categoryMapper.toResponse(adminCategoryService.save(category));
    }

    @DeleteMapping("")
    public void deleteById(@NotNull @RequestParam int id) {
        adminCategoryService.deleteById(id);
    }

    @GetMapping
    public CategoryResponse getById(@NotNull @RequestParam int id) {
        return adminCategoryService.getById(id);
    }

    @PatchMapping("")
    public CategoryResponse updateById(@RequestParam @NotNull int id, @RequestBody @NotNull SaveCategoryRequest request) {
        return adminCategoryService.updateById(id, request);
    }
}
