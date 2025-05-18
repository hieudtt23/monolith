package com.danghieu99.monolith.security.mapper;

import com.danghieu99.monolith.security.dto.account.request.AdminSaveAccountRequest;
import com.danghieu99.monolith.security.dto.account.response.AdminSaveAccountResponse;
import com.danghieu99.monolith.security.dto.auth.request.SignupRequest;
import com.danghieu99.monolith.security.dto.account.response.UserGetAccountDetailsResponse;
import com.danghieu99.monolith.security.dto.account.response.UserGetProfileResponse;
import com.danghieu99.monolith.security.entity.Account;
import com.danghieu99.monolith.security.entity.Role;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AccountMapper {

    @Mappings({
            @Mapping(target = "password", ignore = true)
    })
    Account toAccount(AdminSaveAccountRequest adminSaveRequest);

    AdminSaveAccountResponse toAdminSaveAccountResponse(Account account);

    @Mapping(target = "roles", ignore = true)
    UserGetAccountDetailsResponse toUserAccountDetailsResponse(Account account);

    @Mapping(target = "roles", ignore = true)
    UserGetProfileResponse toUserGetProfileResponse(Account account);

    @Mapping(target = "password", ignore = true)
    Account toAccount(SignupRequest signupRequest);

    @Named("rolesToRoleNames")
    default Set<String> rolesToRoleNames(Set<Role> roles) {
        return roles.stream().map(role -> role.getRole().name()).collect(Collectors.toSet());
    }
}