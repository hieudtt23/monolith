package com.danghieu99.monolith.product.mapper;

import com.danghieu99.monolith.product.dto.request.SaveVariantRequest;
import com.danghieu99.monolith.product.dto.response.VariantDetailsResponse;
import com.danghieu99.monolith.product.entity.jpa.Variant;
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
    VariantDetailsResponse toResponse(Variant variant);

}
