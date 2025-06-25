package com.danghieu99.monolith.ecommerce.product.repository.redis;

import com.danghieu99.monolith.ecommerce.product.entity.redis.RecentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RecentViewRepository extends CrudRepository<RecentView, String> {

    Collection<RecentView> findByAccountUUID(String accountUUID, Sort sort);

    Page<RecentView> findByAccountUUID(String accountUUID, Pageable pageable);
}