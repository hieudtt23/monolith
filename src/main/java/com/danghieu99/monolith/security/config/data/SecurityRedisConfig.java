package com.danghieu99.monolith.security.config.data;

import com.danghieu99.monolith.security.entity.redis.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = {"com.danghieu99.monolith.security.repository.redis"})
@RequiredArgsConstructor
public class SecurityRedisConfig {


    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate<String, Token> redisTokenTemplate() {
        RedisTemplate<String, Token> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
