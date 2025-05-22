package com.danghieu99.monolith.product.service.product;

import com.danghieu99.monolith.product.repository.redis.RecentViewRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RecentlyViewedService {

    private final RecentViewRepository recentViewRepository;

    public void saveRecentlyViewed(@NotBlank String accountUUID, @NotBlank String productUUID) {
        var recentlyViewed = com.danghieu99.monolith.product.entity.redis.RecentView.builder()
                .accountUUID(accountUUID)
                .productUUID(productUUID)
                .timestamp(Instant.now().toEpochMilli())
                .build();
        recentViewRepository.save(recentlyViewed);
    }
}
