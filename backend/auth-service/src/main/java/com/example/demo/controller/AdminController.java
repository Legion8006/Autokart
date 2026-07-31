package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AdminLoginRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.service.AdminAuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminController {

    private final AdminAuthService adminAuthService;

    public AdminController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AdminLoginRequest request) {

        return ResponseEntity.ok(
                adminAuthService.login(request));
    }
}