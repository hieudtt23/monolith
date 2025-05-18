package com.danghieu99.monolith.security.service.auth;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.security.config.auth.AuthTokenProperties;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import com.danghieu99.monolith.security.entity.Account;
import com.danghieu99.monolith.security.repository.jpa.AccountRepository;
import com.danghieu99.monolith.security.util.TokenUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final AccountRepository accountRepository;
    private final AuthTokenProperties authTokenProperties;
    private final UserDetailsServiceImpl userDetailsService;

    public String buildAccessToken(UserDetailsImpl userDetails) {
        SecretKey secretKey = Keys.hmacShaKeyFor(authTokenProperties.getTokenSecretKey().getBytes());
        Claims claims = Jwts.claims()
                .subject(userDetails.getUuid().toString())
                .issuer(authTokenProperties.getTokenIssuer())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(authTokenProperties.getAccessTokenExpireMs())))
                .build();
        return TokenUtil.buildToken(secretKey, claims);
    }

    public String buildRefreshToken(UserDetailsImpl userDetails) {
        SecretKey secretKey = Keys.hmacShaKeyFor(authTokenProperties.getTokenSecretKey().getBytes());
        Claims claims = Jwts.claims()
                .subject(userDetails.getUuid().toString())
                .issuer(authTokenProperties.getTokenIssuer())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(authTokenProperties.getRefreshTokenExpireMs())))
                .build();
        return TokenUtil.buildToken(secretKey, claims);
    }

    public Claims parseClaimsFromToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(authTokenProperties.getTokenSecretKey().getBytes());
        return TokenUtil.parseClaimsFromToken(secretKey, authTokenProperties.getTokenIssuer(), token);
    }

    public UserDetailsImpl getUserDetailsFromToken(String refreshToken) {
        UUID accountUUID = UUID.fromString(parseClaimsFromToken(refreshToken).getSubject());
        Account account = accountRepository.findByUuid(accountUUID)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "Uuid", accountUUID));
        UserDetails userDetails = userDetailsService.loadUserByUsername(account.getUsername());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities());
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    public String parseRefreshTokenFromCookies(Cookie[] cookies) {
        return TokenUtil.parseTokenFromCookies(cookies, authTokenProperties.getRefreshTokenName());
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaimsFromToken(token);
        } catch (JwtException e) {
            return false;
        }
        return true;
    }
}