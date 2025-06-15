package com.danghieu99.monolith.ecommerce.cart.mapper;

import com.danghieu99.monolith.ecommerce.cart.dto.GetCartResponse;
import com.danghieu99.monolith.ecommerce.cart.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    GetCartResponse toGetCartResponse(Cart cart);
}
