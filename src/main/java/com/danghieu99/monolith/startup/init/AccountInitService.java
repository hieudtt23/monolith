package com.danghieu99.monolith.startup.init;

import com.danghieu99.monolith.security.entity.Account;
import com.danghieu99.monolith.security.entity.join.AccountRole;
import com.danghieu99.monolith.security.constant.EGender;
import com.danghieu99.monolith.security.constant.ERole;
import com.danghieu99.monolith.security.repository.jpa.AccountRepository;
import com.danghieu99.monolith.security.repository.jpa.AccountRoleRepository;
import com.danghieu99.monolith.security.repository.jpa.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountInitService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AccountRoleRepository accountRoleRepository;

    @Transactional
    public void init() {
        if (accountRepository.findAll().isEmpty()) {
            Set<AccountRole> accountRoles = new HashSet<>();
            IntStream.range(1, 10).parallel().forEach(i -> {
                var adminAccount = Account.builder()
                        .username("admin" + i)
                        .password(passwordEncoder.encode("adminpassword" + i))
                        .gender(EGender.valueOf(i % 2 == 0 ? "MALE" : "FEMALE"))
                        .email("admin" + i + "@mail.com")
                        .phone("012345678" + i)
                        .fullName("Admin Full Name " + i)
                        .build();
                var savedAdminAccount = accountRepository.save(adminAccount);
                accountRoles.add(AccountRole.builder()
                        .accountId(savedAdminAccount.getId())
                        .roleId(roleRepository.findByRole(ERole.ROLE_ADMIN).get().getId())
                        .build());
            });
            IntStream.range(1, 100).parallel().forEach(i -> {
                var userAccount = (Account.builder()
                        .username("user" + i)
                        .password(passwordEncoder.encode("userpassword" + i))
                        .gender(EGender.valueOf(i % 2 == 0 ? "MALE" : "FEMALE"))
                        .email("user" + i + "@mail.com")
                        .phone("023456789" + i)
                        .fullName("User Full Name " + i)
                        .build());
                var savedUserAccount = accountRepository.save(userAccount);
                accountRoles.add(AccountRole.builder()
                        .accountId(savedUserAccount.getId())
                        .roleId(roleRepository.findByRole(ERole.ROLE_USER).get().getId())
                        .build());
            });
            IntStream.range(1, 50).parallel().forEach(i -> {
                var sellerAccount = Account.builder()
                        .username("seller" + i)
                        .password(passwordEncoder.encode("sellerpassword" + i))
                        .gender(EGender.valueOf(i % 2 == 0 ? "MALE" : "FEMALE"))
                        .email("seller" + i + "@mail.com")
                        .phone("00100123456" + i)
                        .fullName("Seller Full Name " + i)
                        .build();
                var savedSellerAccount = accountRepository.saveAndFlush(sellerAccount);
                accountRoles.add(AccountRole.builder()
                        .accountId(savedSellerAccount.getId())
                        .roleId(roleRepository.findByRole(ERole.ROLE_SELLER).get().getId())
                        .build());
            });
            accountRoleRepository.saveAll(accountRoles);
        }
    }
}