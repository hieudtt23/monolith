package com.danghieu99.monolith.security.dto.account.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
public class UserChangePasswordRequest {

    @NotEmpty
    private String oldPassword;

    @NotEmpty
    private String newPassword;
}
