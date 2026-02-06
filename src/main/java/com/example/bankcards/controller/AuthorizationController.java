package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthenticationResponse;
import com.example.bankcards.dto.LoginRequest;
import com.example.bankcards.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthorizationController {
    private final SecurityService service;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody LoginRequest request) {
        return ResponseEntity.ok().body(AuthenticationResponse.builder().build());
    }

    @GetMapping("/test/secure")
    public String secure() {
        return "secured";
    }
}
