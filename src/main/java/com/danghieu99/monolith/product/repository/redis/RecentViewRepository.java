package com.danghieu99.monolith.product.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentViewRepository extends CrudRepository<com.danghieu99.monolith.product.entity.redis.RecentView, String> {
}
