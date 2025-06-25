package com.danghieu99.monolith.security.config.auth;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.security.entity.Account;
import com.danghieu99.monolith.security.repository.jpa.AccountRepository;
import com.danghieu99.monolith.security.repository.redis.RefreshTokenRepository;
import com.danghieu99.monolith.security.service.auth.AuthTokenService;
import com.danghieu99.monolith.security.util.TokenUtil;
import com.danghieu99.monolith.security.service.auth.UserDetailsServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final AuthTokenService authTokenService;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthTokenProperties authTokenProperties;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String access = TokenUtil.parseTokenFromCookies(request.getCookies(), authTokenProperties.getAccessTokenName());
            if (access != null && !access.isEmpty() && authTokenService.isValid(access)) {
                String accountUUID = authTokenService.parseClaimsFromToken(access).getSubject();
                Account account = accountRepository.findByUuid(UUID.fromString(accountUUID))
                        .orElseThrow(() -> new ResourceNotFoundException("Account", "Uuid", accountUUID));
                UserDetails userDetails = userDetailsService.loadUserByUsername(account.getUsername());
                try {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    null,
                                    userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (AuthenticationException e) {
                    log.error("Authentication exception: {}", e.getMessage());
                    throw e;
                }
            } else {
                String refresh = TokenUtil.parseTokenFromCookies(request.getCookies(), authTokenProperties.getRefreshTokenName());
                if (refresh != null && !refresh.isEmpty()
                        && authTokenService.isValid(refresh)
                        && refreshTokenRepository.existsById(refresh)) {
                    UserDetailsImpl userDetails = authTokenService.getUserDetailsFromToken(refresh);
                    String newAccessToken = authTokenService.buildAccessToken(userDetails);
                    ResponseCookie cookie = ResponseCookie
                            .from(authTokenProperties.getAccessTokenName(), newAccessToken)
                            .httpOnly(true)
                            .secure(true)
                            .sameSite("None")
                            .build();
                    response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                }
            }
        } catch (JwtException e) {
            log.error("Unexpected token exception: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected exception: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}