package com.danghieu99.monolith.product.mapper;

import com.danghieu99.monolith.product.dto.request.SaveCategoryRequest;
import com.danghieu99.monolith.product.dto.response.CategoryResponse;
import com.danghieu99.monolith.product.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    Category toCategory(SaveCategoryRequest request);
}
