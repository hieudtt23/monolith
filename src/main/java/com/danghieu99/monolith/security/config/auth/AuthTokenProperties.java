package com.danghieu99.monolith.security.config.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;;

@ConfigurationProperties(prefix = "jwt")
@Getter
public class AuthTokenProperties {

    @ConstructorBinding
    public AuthTokenProperties(String tokenIssuer, String tokenSecretKey, String accessTokenName, String refreshTokenName, int accessTokenExpireMs, int refreshTokenExpireMs) {
        this.tokenIssuer = tokenIssuer;
        this.tokenSecretKey = tokenSecretKey;
        this.accessTokenName = accessTokenName;
        this.refreshTokenName = refreshTokenName;
        this.accessTokenExpireMs = accessTokenExpireMs;
        this.refreshTokenExpireMs = refreshTokenExpireMs;
    }

    @NotBlank
    private final String tokenIssuer;
    @NotBlank
    private final String tokenSecretKey;
    @NotBlank
    private final String accessTokenName;
    @NotBlank
    private final String refreshTokenName;
    @NotBlank
    private final int accessTokenExpireMs;
    @NotBlank
    private final int refreshTokenExpireMs;
}