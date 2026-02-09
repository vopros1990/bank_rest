package com.example.bankcards.config;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.RoleType;
import com.example.bankcards.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RolesConfiguration {

    private final RoleRepository repository;

    @PostConstruct
    public void initRoles() {
        for (RoleType role : RoleType.values()) {
            if (!repository.existsByRole(role))
                repository.save(Role.from(role));
        }
    }
}
