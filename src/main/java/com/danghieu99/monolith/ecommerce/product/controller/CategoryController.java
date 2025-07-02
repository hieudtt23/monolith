package com.danghieu99.monolith.ecommerce.product.controller;

import com.danghieu99.monolith.ecommerce.product.dto.response.GetCategoryResponse;
import com.danghieu99.monolith.ecommerce.product.service.category.CategoryService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    public Page<GetCategoryResponse> getAllCategories(@PageableDefault Pageable pageable) {
        return categoryService.getAll(pageable);
    }

    @GetMapping("/uuid")
    public GetCategoryResponse getCategoryByUUID(@RequestParam @NotNull String uuid) {
        return categoryService.getByUUID(uuid);
    }

    @GetMapping("/sup-uuid")
    public Page<GetCategoryResponse> getCategoryBySuperCategoryUUID(@RequestParam @NotNull String uuid, @RequestParam @PageableDefault Pageable pageable) {
        return categoryService.getBySuperCategoryUUID(uuid, pageable);
    }

    @GetMapping("/sub-uuid")
    public Page<GetCategoryResponse> getBySubCategoryUUID(@RequestParam @NotNull String uuid, @RequestParam @PageableDefault Pageable pageable) {
        return categoryService.getBySubCategoryUUID(uuid, pageable);
    }

    @GetMapping("/search")
    public Page<GetCategoryResponse> searchByName(@RequestParam @NotNull String name, @RequestParam @PageableDefault Pageable pageable) {
        return categoryService.getByNameContaining(name, pageable);
    }
}
