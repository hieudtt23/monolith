package com.danghieu99.monolith.security.repository.jpa;

import com.danghieu99.monolith.security.entity.Role;
import com.danghieu99.monolith.security.constant.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRole(ERole role);

    @Query("select r from Role r " +
            "join AccountRole ac on r.id = ac.roleId " +
            "where ac.accountId = :accountId")
    Set<Role> findByAccountId(Integer accountId);

    @Query("select r from Role r " +
            "join AccountRole ac on r.id = ac.roleId " +
            "join Account a on ac.accountId = a.id " +
            "where a.uuid = :uuid")
    Set<Role> findByAccountUUID(UUID uuid);
}