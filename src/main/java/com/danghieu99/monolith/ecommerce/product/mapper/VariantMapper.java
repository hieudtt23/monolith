package com.danghieu99.monolith.ecommerce.product.mapper;

import com.danghieu99.monolith.ecommerce.product.dto.request.SaveVariantRequest;
import com.danghieu99.monolith.ecommerce.product.dto.response.GetVariantDetailsResponse;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Variant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface VariantMapper {
    Variant toVariant(SaveVariantRequest request);

    @Mappings(
            {@Mapping(target = "attributes", ignore = true), @Mapping(target = "imageToken", ignore = true)})
    GetVariantDetailsResponse toResponse(Variant variant);

}
