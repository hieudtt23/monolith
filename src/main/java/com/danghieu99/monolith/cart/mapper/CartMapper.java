package com.danghieu99.monolith.cart.mapper;

import com.danghieu99.monolith.cart.dto.GetCartResponse;
import com.danghieu99.monolith.cart.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    GetCartResponse toGetCartResponse(Cart cart);
}
