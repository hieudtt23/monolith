package com.danghieu99.monolith.product.service.product;

import com.danghieu99.monolith.product.entity.redis.RecentView;
import com.danghieu99.monolith.product.repository.redis.RecentViewRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentlyViewedService {

    private final RecentViewRepository recentViewRepository;

    @Transactional
    public void saveRecentlyViewed(@NotBlank String accountUUID,
                                   @NotBlank String productUUID) {
        var recentlyViewed = com.danghieu99.monolith.product.entity.redis.RecentView.builder()
                .accountUUID(accountUUID)
                .productUUID(productUUID)
                .timestamp(Instant.now().toEpochMilli())
                .build();
        recentViewRepository.save(recentlyViewed);
    }

    public List<String> findByAccountUUID(@NotBlank String accountUUID, @NotNull Sort sort) {
        return recentViewRepository.findByAccountUUID(accountUUID, sort)
                .stream().map(RecentView::toString).toList();
    }
}
