package com.example.bankcards.security;

import com.example.bankcards.dto.response.AuthenticationResponse;
import com.example.bankcards.dto.request.AuthenticationRequest;
import com.example.bankcards.dto.request.UserRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.RoleType;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AlreadyExistsException;
import com.example.bankcards.exception.UserBlockedException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Validated
public class SecurityService {

    private final UserMapper mapper;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticateUser(@Valid AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();

        if (!userDetails.isEnabled())
            throw new UserBlockedException("Доступ запрещен! Пользователь заблокирован");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return AuthenticationResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(roles)
                .token(jwtUtils.generateJwtToken(userDetails.getUsername()))
                .build();
    }

    @Transactional
    public UserResponse registerUser(@Valid UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AlreadyExistsException("Имя пользователя уже занято. Выберите другое");

        if (userRepository.existsByEmail(request.getEmail()))
            throw new AlreadyExistsException("Email пользователя уже существует");

        Role role = roleRepository.findByRole(RoleType.USER).orElseGet(() -> Role.from(RoleType.USER));

        User user = mapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(role));

        return mapper.toResponse(userRepository.save(user));
    }


}
