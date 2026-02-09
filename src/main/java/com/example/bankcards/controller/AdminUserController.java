package com.example.bankcards.controller;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Users (ADMIN)", description = "Операции с пользователями. Только для администраторов")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService service;

    @Operation(
            summary = "Список всех пользователей",
            description = "Возвращает список зарегистрированных пользователей"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)")
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(service.findAll(page, limit));
    }

    @Operation(
            summary = "Получение пользователя по ID",
            description = "Возвращает полную информацию о пользователе"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Удаление пользователя",
            description = "Удаляет пользователя по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Карта удалена"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Активация пользователя",
            description = "Активирует пользователя с указанным ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Карта удалена"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
    })
    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable long id) {
        service.activate(id);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Блокировка пользователя",
            description = "Блокирует пользователя с указанным ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Карта удалена"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
    })
    @PutMapping("/{id}/block")
    public ResponseEntity<Void> block(@PathVariable long id) {
        service.block(id);

        return ResponseEntity.ok().build();
    }
}
