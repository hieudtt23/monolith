package com.danghieu99.monolith.security.dto.auth.response;

import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

@ToString
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
public class LoginResponseBody {

    @NotNull
    private String username;

    @NotNull
    private Set<String> roles;

    private String message;
}
