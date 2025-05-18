package com.danghieu99.monolith.security.repository.jpa;

import com.danghieu99.monolith.security.entity.join.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Integer> {
}
