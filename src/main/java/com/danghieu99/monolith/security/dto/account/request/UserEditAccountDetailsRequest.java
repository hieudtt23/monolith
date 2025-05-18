package com.danghieu99.monolith.security.dto.account.request;

import com.danghieu99.monolith.security.constant.EGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
public class UserEditAccountDetailsRequest {

    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$")
    private String username;

    @Size(min = 4, max = 20)
    @Pattern(regexp = "^[a-zA-Z]+$")
    private EGender gender;

    @Size(min = 5, max = 255)
    @Pattern(regexp = "^[\\p{L}\\p{M}'\\-.\\s]$")
    private String fullName;

    @Size(min = 5, max = 255)
    @Email
    private String email;

    @Size(min = 3, max = 25)
    @Pattern(regexp = "^[0-9]+$")
    private String phone;
}
