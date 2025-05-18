package com.danghieu99.monolith.security.repository.redis;

import com.danghieu99.monolith.security.entity.redis.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends CrudRepository<Token, UUID> {
    boolean existsByValue(String token);

    boolean existsByAccountUUID(String accountUUID);

    Optional<Token> findByAccountUUID(String accountUUID);

    Optional<Token> findByValue(String token);

    void deleteByValue(String token);

    void deleteByAccountUUID(String accountUUID);
}