package com.danghieu99.monolith.security.controller;

import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import com.danghieu99.monolith.security.dto.account.request.UserChangePasswordRequest;
import com.danghieu99.monolith.security.dto.account.request.UserEditAccountDetailsRequest;
import com.danghieu99.monolith.security.service.account.UserAccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/account")
@Validated
public class UserAccountController {

    private final UserAccountService uAccountService;

    public UserAccountController(UserAccountService uAccountService) {
        this.uAccountService = uAccountService;
    }

    @GetMapping("")
    public ResponseEntity<?> getPrivateAccountDetails(@RequestParam String uuid) {
        return ResponseEntity.ok(uAccountService.getPrivateAccountDetailsByUUID(uuid));
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editAccountDetails(@Valid @RequestBody UserEditAccountDetailsRequest request,
                                                @NotNull @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(uAccountService.editAccountDetails(request, userDetails));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UserChangePasswordRequest request,
                                            @NotNull @AuthenticationPrincipal UserDetailsImpl userDetails) {
        uAccountService.changeUserAccountPassword(request, userDetails);
        return ResponseEntity.ok("Password change success!");
    }
}