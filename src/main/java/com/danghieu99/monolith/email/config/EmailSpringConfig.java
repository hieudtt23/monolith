package com.danghieu99.monolith.email.config;

import com.danghieu99.monolith.email.repository.EmailTemplateRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = EmailTemplateRepository.class)
public class EmailSpringConfig {
}
