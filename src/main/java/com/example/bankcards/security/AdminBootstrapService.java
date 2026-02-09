package com.example.bankcards.security;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.RoleType;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminBootstrapService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void bootstrapAdmin(String password) {
        String passwordEncoded = passwordEncoder.encode(password);

        Role role = roleRepository.findByRole(RoleType.ADMIN)
                .orElseGet(() -> Role.from(RoleType.ADMIN));

        User admin = userRepository.findByUsername("admin")
                .orElseGet(() -> {
                    User user = User.builder()
                            .username("admin")
                            .firstName("admin")
                            .lastName("admin")
                            .email("admin@example.com")
                            .password(passwordEncoded)
                            .roles(Set.of(role))
                            .build();

                    return userRepository.save(user);
                });

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            admin.setPassword(passwordEncoded);
            userRepository.save(admin);
        }
    }
}
