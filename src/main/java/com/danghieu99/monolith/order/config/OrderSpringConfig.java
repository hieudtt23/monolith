package com.danghieu99.monolith.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.danghieu99.monolith.order.repository")
public class OrderSpringConfig {
}
