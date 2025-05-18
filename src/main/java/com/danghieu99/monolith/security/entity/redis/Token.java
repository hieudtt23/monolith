package com.danghieu99.monolith.security.entity.redis;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@RedisHash("tokens")
public class Token {

    @Id
    private String value;

    @Indexed
    private String accountUUID;

    @NotNull
    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;
}