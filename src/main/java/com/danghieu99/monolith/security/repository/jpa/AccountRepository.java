package com.danghieu99.monolith.security.repository.jpa;

import com.danghieu99.monolith.security.constant.EAccountStatus;
import com.danghieu99.monolith.security.entity.Account;
import com.danghieu99.monolith.security.constant.ERole;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findById(int id);

    Optional<Account> findByUsername(String username);

    @Query("select a from Account a " +
            "where a.username like concat('%',:username, '%')")
    Page<Account> findByUsernameContaining(String username, Pageable pageable);

    Optional<Account> findByUuid(UUID uuid);

    Optional<Account> findByEmail(String email);

    @Query("select a from Account a " +
            "where a.email like concat('%', :email, '%')")
    List<Account> findByEmailContains(String email);

    @Query("select a from Account a " +
            "where a.email like concat('%', :email, '%')")
    Page<Account> findByEmailContains(String email, Pageable pageable);

    Optional<Account> findByPhone(String phone);

    @Query("select a from Account a " +
            "where a.phone like concat('%', :phone, '%')")
    List<Account> findByPhoneContaining(String phone);

    @Query("select a from Account a " +
            "where a.phone like concat('%', :phone, '%')")
    Page<Account> findByPhoneContaining(String phone, Pageable pageable);

    @Query("select a from Account a " +
            "where a.phone like concat('%', :phone)")
    List<Account> findByPhoneStartingWith(String phone);

    @Query("select a from Account a " +
            "where a.phone like concat('%', :phone)")
    Page<Account> findByPhoneStartingWith(String phone, Pageable pageable);

    @Query("select a from Account a " +
            "join AccountRole ar on ar.accountId = a.id " +
            "join Role r on ar.roleId = r.id " +
            "where r.role = :eRole")
    List<Account> findByERole(ERole eRole);

    @Query("select a from Account a " +
            "join AccountRole ar on ar.accountId = a.id " +
            "join Role r on ar.roleId = r.id " +
            "where r.role = :eRole")
    Page<Account> findByERole(ERole eRole, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Account a " +
            "set a.status = :status " +
            "where a.id = :id")
    void updateAccountStatusById(int id, EAccountStatus status);

    @Transactional
    @Modifying
    @Query("update Account a " +
            "set a.status = :status " +
            "where a.uuid = :uuid")
    void updateAccountStatusByUUID(UUID uuid, EAccountStatus status);

    @Query("select a.email from Account a " +
            "where a.uuid = :uuid")
    String findEmailByAccountUUID(UUID uuid);
}