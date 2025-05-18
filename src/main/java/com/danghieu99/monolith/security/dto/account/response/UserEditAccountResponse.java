package com.danghieu99.monolith.security.dto.account.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEditAccountResponse {
    private boolean success;
    private String message;
}
