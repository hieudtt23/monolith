package com.danghieu99.monolith.security.dto.auth.response;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
public class LogoutResponseBody {

    private String message;
}
