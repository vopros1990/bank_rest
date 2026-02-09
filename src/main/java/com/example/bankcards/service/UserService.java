package com.example.bankcards.service;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.UserStatus;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class UserService {

    private final UserMapper mapper;

    private final UserRepository repository;

    public List<UserResponse> findAll(
            @PositiveOrZero(message = "Укажите номер страницы") int page,
            @Positive(message = "Укажите лимит") int limit) {
        return repository.findAll(PageRequest.of(page, limit))
                .map(mapper::toResponse).toList();
    }

    public UserResponse findById(
            @Positive(message = "ID пользователя не может быть меньше 1") Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с указанным ID не найден"));

        return mapper.toResponse(user);
    }

    public void deleteById(@Positive(message = "ID пользователя не может быть меньше 1") Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с указанным ID не найден"));

        repository.deleteById(id);
    }

    @Transactional
    public void activate(@Positive(message = "ID пользователя не может быть меньше 1") Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с указанным ID не найден"));

        user.setStatus(UserStatus.ACTIVE);

        repository.save(user);
    }

    @Transactional
    public void block(@Positive(message = "ID пользователя не может быть меньше 1") Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с указанным ID не найден"));

        user.setStatus(UserStatus.BLOCKED);

        repository.save(user);
    }
}
