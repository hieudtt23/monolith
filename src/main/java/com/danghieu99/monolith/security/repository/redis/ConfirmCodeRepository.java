package com.danghieu99.monolith.security.repository.redis;

import com.danghieu99.monolith.security.entity.redis.ConfirmCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConfirmCodeRepository extends CrudRepository<ConfirmCode, String> {

    Optional<ConfirmCode> findByValue(String value);
}