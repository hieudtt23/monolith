package com.danghieu99.monolith.ecommerce.product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.danghieu99.monolith.ecommerce.product.repository.jpa")
public class ProductSpringConfig {
}