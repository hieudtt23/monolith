package com.danghieu99.monolith.ecommerce.product.repository.jpa;

import com.danghieu99.monolith.ecommerce.product.entity.jpa.GlobalAttributeType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalAttributeRepository extends JpaRepository<GlobalAttributeType, Integer> {
}
