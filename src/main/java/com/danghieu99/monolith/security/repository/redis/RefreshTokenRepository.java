package com.danghieu99.monolith.security.repository.redis;

import com.danghieu99.monolith.security.entity.redis.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<Token, String> {

    boolean existsByAccountUUID(String accountUUID);

    Optional<Token> findByAccountUUID(String accountUUID);

    void deleteByAccountUUID(String accountUUID);
}