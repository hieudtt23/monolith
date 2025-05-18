package com.danghieu99.monolith.security.service.account;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.security.dto.account.response.UserGetProfileResponse;
import com.danghieu99.monolith.security.mapper.AccountMapper;
import com.danghieu99.monolith.security.repository.jpa.AccountRepository;
import com.danghieu99.monolith.security.repository.jpa.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final RoleRepository roleRepository;

    public UserGetProfileResponse getPublicAccountDetailsByUUID(String uuid) {
        var account = accountRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new ResourceNotFoundException("Account", "uuid", uuid));
        var profileResponse = accountMapper.toUserGetProfileResponse(account);
        profileResponse.setRoles(accountMapper.rolesToRoleNames(roleRepository.findByAccountId(account.getId())));
        return profileResponse;
    }
}
