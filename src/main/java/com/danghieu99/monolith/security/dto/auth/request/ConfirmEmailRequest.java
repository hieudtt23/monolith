package com.danghieu99.monolith.security.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmEmailRequest {

    @NotBlank
    private final String code;
}
