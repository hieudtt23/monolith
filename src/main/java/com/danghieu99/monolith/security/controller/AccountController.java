package com.danghieu99.monolith.security.controller;

import com.danghieu99.monolith.security.service.account.AccountService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@Validated
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/profile")
    public ResponseEntity<?> getPrivateAccountDetails(@RequestParam @NotEmpty String uuid) {
        return ResponseEntity.ok(accountService.getPublicAccountDetailsByUUID(uuid));
    }
}
