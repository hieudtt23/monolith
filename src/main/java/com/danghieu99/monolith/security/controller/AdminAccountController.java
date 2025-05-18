package com.danghieu99.monolith.security.controller;

import com.danghieu99.monolith.security.dto.account.request.AdminSaveAccountRequest;
import com.danghieu99.monolith.security.service.account.AdminAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/accounts")
@RequiredArgsConstructor
@Validated
public class AdminAccountController {

    private final AdminAccountService adminAccountService;

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable int id) {
        return ResponseEntity.ok(adminAccountService.getById(id));
    }

    @GetMapping("/username")
    public ResponseEntity<?> getAccountByUsername(@RequestParam String username) {
        return ResponseEntity.ok(adminAccountService.getByUsername(username));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAccount(@Valid @RequestBody AdminSaveAccountRequest request) {
        return ResponseEntity.ok(adminAccountService.addAccount(request));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateAccount(@RequestParam int id, @Valid @RequestBody AdminSaveAccountRequest request) {
        return ResponseEntity.ok(adminAccountService.updateAccount(id, request));
    }

    @DeleteMapping("/deactivate")
    public ResponseEntity<?> deactivateAccount(@RequestParam int id) {
        adminAccountService.deactivateAccountById(id);
        return ResponseEntity.ok("Deactivate account success");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestParam int id) {
        adminAccountService.deleteAccountById(id);
        return ResponseEntity.ok("Delete account success!");
    }
}