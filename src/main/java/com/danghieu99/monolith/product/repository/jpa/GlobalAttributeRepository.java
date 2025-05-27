package com.danghieu99.monolith.product.repository.jpa;

import com.danghieu99.monolith.product.entity.jpa.GlobalAttributeType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalAttributeRepository extends JpaRepository<GlobalAttributeType, Integer> {
}
