package com.danghieu99.monolith.security.dto.auth.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmEmailResponse {

    @NotNull
    private final boolean success;

    @NotBlank
    private final String message;
}
