package com.danghieu99.monolith.security.config.data;

import com.danghieu99.monolith.security.config.auth.AuthTokenProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAsync
@EnableTransactionManagement
@EnableConfigurationProperties({AuthTokenProperties.class})
@EnableJpaRepositories(basePackages = {"com.danghieu99.monolith.security.repository.jpa"})
public class SecuritySpringConfig {

}