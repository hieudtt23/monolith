package com.danghieu99.monolith.ecommerce.product.mapper;

import com.danghieu99.monolith.ecommerce.product.entity.jpa.Attribute;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AttributeMapper {

    default Set<Attribute> toAttributes(Map<String, String> attributeMap) {
        Set<Attribute> attributes = new HashSet<>();
        attributeMap.forEach((key, value) -> Attribute.builder()
                .type(key)
                .value(value)
                .build());
        return attributes;
    }
}
