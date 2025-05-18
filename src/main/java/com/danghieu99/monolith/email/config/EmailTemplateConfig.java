package com.danghieu99.monolith.email.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Set;

@Configuration
public class EmailTemplateConfig {

    @Bean
    public SpringTemplateEngine emailTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addTemplateResolver(emailClassLoaderTemplateResolver());
        engine.addTemplateResolver(emailDatabaseTemplateResolver());
        return engine;
    }

    protected ITemplateResolver emailClassLoaderTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setName("classLoaderTemplateResolver");
        templateResolver.setOrder(1);
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setResolvablePatterns(Set.of("emails/*"));
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCheckExistence(true);
        templateResolver.setCacheable(true);
        templateResolver.setCacheTTLMs(60000L);
        return templateResolver;
    }

    protected ITemplateResolver emailDatabaseTemplateResolver() {
        StringTemplateResolver templateResolver = new DatabaseTemplateResolver();
        templateResolver.setName("DatabaseTemplateResolver");
        templateResolver.setOrder(2);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setResolvablePatterns(Set.of("emails/*"));
        templateResolver.setCheckExistence(true);
        templateResolver.setCacheable(true);
        templateResolver.setCacheTTLMs(60000L);
        return templateResolver;
    }
}