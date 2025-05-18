package com.danghieu99.monolith.security.dto.account.response;

import com.danghieu99.monolith.security.constant.EGender;
import com.danghieu99.monolith.security.constant.ERole;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AdminSaveAccountResponse {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private EGender gender;

    @NotNull
    private String fullName;

    @NotNull
    private String email;

    private String phone;

    private Set<ERole> roles;
}
