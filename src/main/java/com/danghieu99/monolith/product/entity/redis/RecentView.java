package com.danghieu99.monolith.product.entity.redis;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Data
@RedisHash("recent-view")
public class RecentView {

    @Builder
    public RecentView(String accountUUID, String productUUID, long timestamp) {
        this.id = accountUUID + ":" + productUUID;
        this.accountUUID = accountUUID;
        this.productUUID = productUUID;
        this.timestamp = timestamp;
    }

    @Id
    private String id;

    @NotBlank
    private final String accountUUID;

    @NotBlank
    private final String productUUID;

    @NotBlank
    private final long timestamp;

    @TimeToLive(unit = TimeUnit.DAYS)
    private final int ttlDays = 14;
}