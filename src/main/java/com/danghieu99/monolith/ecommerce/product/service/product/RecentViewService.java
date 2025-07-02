package com.danghieu99.monolith.ecommerce.product.service.product;

import com.danghieu99.monolith.ecommerce.product.entity.redis.RecentView;
import com.danghieu99.monolith.ecommerce.product.repository.redis.RecentViewRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RecentViewService {

    private final RecentViewRepository recentViewRepository;

    @Transactional
    protected void saveRecentlyViewed(@NotBlank String accountUUID, @NotBlank String productUUID) {
        RecentView recentlyViewed = RecentView.builder()
                .accountUUID(accountUUID)
                .productUUID(productUUID)
                .timestamp(Instant.now().toEpochMilli())
                .build();
        recentViewRepository.save(recentlyViewed);
    }


}
