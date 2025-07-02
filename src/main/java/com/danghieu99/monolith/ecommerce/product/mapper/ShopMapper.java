package com.danghieu99.monolith.ecommerce.product.mapper;

import com.danghieu99.monolith.ecommerce.product.dto.request.SaveShopRequest;
import com.danghieu99.monolith.ecommerce.product.dto.response.GetShopDetailsResponse;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Shop;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ShopMapper {

    Shop toShop(SaveShopRequest request);

    GetShopDetailsResponse toResponse(Shop shop);
}
