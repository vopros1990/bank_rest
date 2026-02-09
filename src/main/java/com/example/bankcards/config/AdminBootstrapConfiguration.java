package com.example.bankcards.config;

import com.example.bankcards.security.AdminBootstrapService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AdminBootstrapConfiguration {

    private final AdminBootstrapService service;

    @Value("${bootstrap.admin.password}")
    private String password;

    @PostConstruct
    public void bootstrapAdmin() {
        service.bootstrapAdmin(password);
    }
}
