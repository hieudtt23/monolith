package com.danghieu99.monolith.security.controller;

import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import com.danghieu99.monolith.security.dto.auth.request.ConfirmEmailRequest;
import com.danghieu99.monolith.security.dto.auth.request.LoginRequest;
import com.danghieu99.monolith.security.dto.auth.request.SignupRequest;
import com.danghieu99.monolith.security.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid SignupRequest request) {
        return authenticationService.register(request);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return authenticationService.logout(request);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout-all")
    public ResponseEntity<?> deleteAllTokens(@NotNull @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return authenticationService.deleteAllTokens(userDetails);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAuthentication(@NotNull @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return authenticationService.refreshAuthentication(userDetails);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@RequestBody @Valid ConfirmEmailRequest request) {
        return authenticationService.confirmEmail(request);
    }
}