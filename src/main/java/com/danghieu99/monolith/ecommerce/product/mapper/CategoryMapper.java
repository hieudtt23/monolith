package com.danghieu99.monolith.ecommerce.product.mapper;

import com.danghieu99.monolith.ecommerce.product.dto.request.SaveCategoryRequest;
import com.danghieu99.monolith.ecommerce.product.dto.response.GetCategoryResponse;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CategoryMapper {

    GetCategoryResponse toResponse(Category category);

    Category toCategory(SaveCategoryRequest request);
}
