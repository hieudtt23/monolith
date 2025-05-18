package com.danghieu99.monolith.security.service.auth;

import com.danghieu99.monolith.common.exception.EntityExistsException;
import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.email.dto.SendEmailRequest;
import com.danghieu99.monolith.email.service.SendEmailToKafkaService;
import com.danghieu99.monolith.security.config.auth.AuthTokenProperties;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import com.danghieu99.monolith.security.constant.EAccountStatus;
import com.danghieu99.monolith.security.dto.auth.request.ConfirmEmailRequest;
import com.danghieu99.monolith.security.dto.auth.request.LoginRequest;
import com.danghieu99.monolith.security.dto.auth.request.SignupRequest;
import com.danghieu99.monolith.security.dto.auth.response.*;
import com.danghieu99.monolith.security.entity.Account;
import com.danghieu99.monolith.security.entity.redis.Token;
import com.danghieu99.monolith.security.constant.ERole;
import com.danghieu99.monolith.security.entity.join.AccountRole;
import com.danghieu99.monolith.security.mapper.AccountMapper;
import com.danghieu99.monolith.security.repository.jpa.AccountRepository;
import com.danghieu99.monolith.security.repository.jpa.AccountRoleRepository;
import com.danghieu99.monolith.security.repository.jpa.RoleRepository;
import com.danghieu99.monolith.security.repository.redis.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountMapper accountMapper;
    private final AuthTokenProperties authTokenProperties;
    private final AuthTokenService authTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final RefreshTokenService refreshTokenService;
    private final ConfirmCodeService confirmCodeService;
    private final SendEmailToKafkaService sendEmailToKafkaService;

    @Value("${authentication.email.from}")
    private String fromEmail;

    @Transactional
    public ResponseEntity<?> authenticate(@NotNull final LoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                        request.getPassword())
                );
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String refreshToken = authTokenService.buildRefreshToken(userDetails);
        String accessToken = authTokenService.buildAccessToken(userDetails);
        ResponseCookie refreshTokenCookie = ResponseCookie
                .from(authTokenProperties.getRefreshTokenName(), refreshToken)
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .maxAge(TimeUnit.MILLISECONDS.toSeconds(authTokenProperties.getRefreshTokenExpireMs()))
                .build();
        ResponseCookie accessTokenCookie = ResponseCookie
                .from(authTokenProperties.getAccessTokenName(), accessToken)
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .maxAge(TimeUnit.MILLISECONDS.toSeconds(authTokenProperties.getAccessTokenExpireMs()))
                .build();

        var saveToken = Token.builder()
                .accountUUID(userDetails.getUuid())
                .value(refreshToken)
                .expiration(authTokenProperties.getRefreshTokenExpireMs())
                .build();
        refreshTokenRepository.save(saveToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add("Access-Control-Allow-Credentials", "true");
        LoginResponseBody body = LoginResponseBody.builder()
                .username(userDetails.getUsername())
                .roles(roles)
                .message("Login success!")
                .build();
        return ResponseEntity.ok()
                .headers(headers)
                .body(body);
    }

    @Transactional
    public ResponseEntity<?> register(@NotNull final SignupRequest request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new EntityExistsException("Account", "username", request.getUsername());
        }
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EntityExistsException("Account", "email", request.getEmail());
        }
        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        Account savedAccount = accountRepository.save(account);
        int roleId = roleRepository.findByRole(ERole.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "role", ERole.ROLE_USER)).getId();
        AccountRole accountRole = AccountRole.builder()
                .accountId(savedAccount.getId())
                .roleId(roleId)
                .build();
        accountRoleRepository.save(accountRole);

        //replace with template
        String code = UUID.randomUUID().toString();
        confirmCodeService.save(savedAccount.getUuid().toString(), code, "email");
        sendEmailToKafkaService.sendToKafka(SendEmailRequest.builder()
                .from(List.of(fromEmail))
                .to(List.of(request.getEmail()))
                .subject("email confirm code")
                .plainText(code)
                .build());

        Set<String> roles = new HashSet<>();
        roles.add(ERole.ROLE_USER.toString());
        SignupResponseBody responseBody = SignupResponseBody.builder()
                .username(savedAccount.getUsername())
                .roles(roles).message("Signup success!")
                .build();
        return ResponseEntity.ok().body(responseBody);
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> logout(@NotNull final HttpServletRequest request) {
        String refreshToken = authTokenService.parseRefreshTokenFromCookies(request.getCookies());
        if (refreshToken != null
                && !refreshToken.isEmpty()
                && refreshTokenService.existsByValue(refreshToken)) {
            refreshTokenService.deleteByValue(refreshToken);
        }
        LogoutResponseBody response = LogoutResponseBody
                .builder()
                .message("Logout success!")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> deleteAllTokens(@NotNull final UserDetailsImpl userDetails) {
        refreshTokenRepository.deleteByAccountUUID(userDetails.getUuid());
        LogoutResponseBody response = LogoutResponseBody.builder()
                .message("Delete all account tokens success!")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> refreshAuthentication(@NotNull final UserDetailsImpl userDetails) {
        String accessToken = authTokenService.buildAccessToken(userDetails);
        ResponseCookie accessTokenCookie = ResponseCookie
                .from(authTokenProperties.getAccessTokenName(), accessToken)
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .maxAge(TimeUnit.MILLISECONDS.toSeconds(authTokenProperties.getAccessTokenExpireMs()))
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add("Access-Control-Allow-Credentials", "true");
        return ResponseEntity.ok().headers(headers).body("Refresh token success!");
    }

    @Transactional
    public ResponseEntity<ConfirmEmailResponse> confirmEmail(@NotNull final ConfirmEmailRequest request) {
        String accountUUID = confirmCodeService.validate(request.getCode());
        accountRepository.updateAccountStatusByUUID(UUID.fromString(accountUUID), EAccountStatus.ACCOUNT_ACTIVE);
        return ResponseEntity.ok()
                .body(ConfirmEmailResponse.builder()
                        .success(true)
                        .message("Confirm email success")
                        .build());
    }
}