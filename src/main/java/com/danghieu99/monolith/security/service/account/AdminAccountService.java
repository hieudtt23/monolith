package com.danghieu99.monolith.security.service.account;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.security.constant.EAccountStatus;
import com.danghieu99.monolith.security.constant.ERole;
import com.danghieu99.monolith.security.dto.account.request.AdminSaveAccountRequest;
import com.danghieu99.monolith.security.dto.account.response.AdminSaveAccountResponse;
import com.danghieu99.monolith.security.entity.Account;
import com.danghieu99.monolith.security.entity.join.AccountRole;
import com.danghieu99.monolith.security.mapper.AccountMapper;
import com.danghieu99.monolith.security.repository.jpa.AccountRepository;
import com.danghieu99.monolith.security.repository.jpa.AccountRoleRepository;
import com.danghieu99.monolith.security.service.role.RoleService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final RoleService roleService;
    private final AccountRoleRepository accountRoleRepository;


    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Page<Account> getAll(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    public List<Account> getByRole(ERole role) {
        return accountRepository.findByERole(role);
    }

    public Account getById(@NotNull Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "Id", id));
    }

    public Account getByUsername(@NotNull String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "Username", username));
    }

    public Account getByEmail(@NotNull String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "Email", email));
    }

    public Account getByPhone(@NotNull String phone) {
        return accountRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "Phone", phone));
    }

    public Account getByUUID(@NotNull UUID uuid) {
        return accountRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "Uuid", uuid.toString()));
    }

    @Transactional
    public AdminSaveAccountResponse addAccount(AdminSaveAccountRequest request) {
        var newAccount = accountRepository.save(accountMapper.toAccount(request));
        List<AccountRole> accountRoles = new ArrayList<>();
        for (var role : request.getRoles()) {
            accountRoles.add(AccountRole.builder().roleId(roleService.getByERole(role).getId()).accountId(newAccount.getId()).build());
        }
        accountRoleRepository.saveAll(accountRoles);
        return accountMapper.toAdminSaveAccountResponse(accountRepository.save(newAccount));
    }

    @Transactional
    public AdminSaveAccountResponse updateAccount(int id, AdminSaveAccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
        if (request.getUsername() != null) account.setUsername(request.getUsername());
        if (request.getPassword() != null) account.setPassword(request.getPassword());
        if (request.getEmail() != null) account.setEmail(request.getEmail());
        if (request.getGender() != null) account.setGender(request.getGender());
        if (request.getPhone() != null) account.setPhone(request.getPhone());
        if (request.getFullName() != null) account.setFullName(request.getFullName());
        var updatedAccount = accountRepository.save(account);
        var response = accountMapper.toAdminSaveAccountResponse(updatedAccount);
        if (request.getRoles() != null) {
            Set<AccountRole> accountRoles = new HashSet<>();
            for (ERole role : request.getRoles()) {
                accountRoles.add(AccountRole.builder().roleId(roleService.getByERole(role).getId()).accountId(account.getId()).build());
            }
            var updatedAccountRoles = accountRoleRepository.saveAll(accountRoles);
            response.setRoles(updatedAccountRoles.stream().map(accountRole -> roleService.getById(accountRole.getRoleId()).getRole()).collect(Collectors.toSet()));
        }
        return response;
    }

    public void deleteAccountById(int id) {
        accountRepository.deleteById(id);
    }

    public void deactivateAccountById(int id) {
        accountRepository.updateAccountStatusById(id, EAccountStatus.ACCOUNT_INACTIVE);
    }
}
