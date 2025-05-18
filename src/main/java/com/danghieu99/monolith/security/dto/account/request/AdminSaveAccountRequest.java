package com.danghieu99.monolith.security.dto.account.request;

import com.danghieu99.monolith.security.constant.EGender;
import com.danghieu99.monolith.security.constant.ERole;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.util.Set;

@Data
@Builder
public class AdminSaveAccountRequest {

    @NotNull
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$")
    private String username;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @NotNull
    @Size(min = 4, max = 6)
    @Pattern(regexp = "^[a-zA-Z]+$")
    private EGender gender;

    @NotNull
    @Size(min = 5, max = 255)
    @Pattern(regexp = "^[\\p{L}\\p{M}'\\-.\\s]$")
    private String fullName;

    @NotNull
    @Size(min = 5, max = 255)
    @Email
    private String email;

    @NotNull
    @Size(min = 3, max = 25)
    @Pattern(regexp = "^[0-9]+$")
    private String phone;

    @NotEmpty
    @Size(min = 1, max = 3)
    private Set<ERole> roles;
}