package com.danghieu99.monolith.ecommerce.product.mapper;

import com.danghieu99.monolith.ecommerce.product.dto.request.SaveProductRequest;
import com.danghieu99.monolith.ecommerce.product.dto.response.ProductDetailsResponse;
import com.danghieu99.monolith.ecommerce.product.dto.response.ProductResponse;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {

    Product toProduct(SaveProductRequest request);

    @Mappings({@Mapping(target = "categories", ignore = true),
            @Mapping(target = "shopUUID", ignore = true),
            @Mapping(target = "variants", ignore = true),
            @Mapping(target = "imageTokens", ignore = true),
            @Mapping(target = "uuid", ignore = true),})
    ProductDetailsResponse toGetProductDetailsResponse(Product product);

    @Mapping(target = "imageToken", ignore = true)
    ProductResponse toProductResponse(Product product);
}
