package com.danghieu99.monolith.ecommerce.cart.config;

import com.danghieu99.monolith.ecommerce.cart.repository.CartRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackageClasses = CartRepository.class)
public class CartSpringConfig {
}