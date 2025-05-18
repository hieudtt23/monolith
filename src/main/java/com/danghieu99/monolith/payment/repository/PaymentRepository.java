package com.danghieu99.monolith.payment.repository;

import com.danghieu99.monolith.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
