package com.danghieu99.monolith.product.repository.redis;

import com.danghieu99.monolith.product.entity.redis.RecentView;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecentViewRepository extends CrudRepository<RecentView, String> {
    List<RecentView> findByAccountUUID(String accountUUID, Sort sort);
}