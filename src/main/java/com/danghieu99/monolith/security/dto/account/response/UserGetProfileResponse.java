package com.danghieu99.monolith.security.dto.account.response;

import com.danghieu99.monolith.security.constant.EGender;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserGetProfileResponse {

    private String username;

    private EGender gender;

    private String fullName;

    private String email;

    private String phone;

    private Set<String> roles;
}
