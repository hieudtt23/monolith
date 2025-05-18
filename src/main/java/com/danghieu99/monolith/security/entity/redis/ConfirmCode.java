package com.danghieu99.monolith.security.entity.redis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@RedisHash("confirm_codes")
public class ConfirmCode {

    @Id
    private String accountUUID;

    @NotBlank
    private String value;

    @NotBlank
    private String type;

    @NotNull
    @TimeToLive(unit = TimeUnit.MINUTES)
    private long expiration;
}
