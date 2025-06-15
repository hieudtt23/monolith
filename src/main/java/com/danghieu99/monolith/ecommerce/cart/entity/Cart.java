package com.danghieu99.monolith.ecommerce.cart.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@RedisHash("carts")
@Builder
public class Cart {

    @Id
    private String accountUUID;

    //variantUUID : quantity
    private Map<String, Integer> items;
}