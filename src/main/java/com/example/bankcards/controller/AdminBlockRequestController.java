package com.example.bankcards.controller;

import com.example.bankcards.dto.filter.CardBlockRequestFilter;
import com.example.bankcards.dto.response.CardBlockRequestResponse;
import com.example.bankcards.security.AuthUserDetails;
import com.example.bankcards.service.CardBlockRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/block-requests")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Block Requests (ADMIN)", description = "Операции с заявками на блокировку карт. Только для администраторов")
@RequiredArgsConstructor
public class AdminBlockRequestController {

    private final CardBlockRequestService service;

    @Operation(
            summary = "Список всех заявок на блокировку карт",
            description = "Возвращает список зарегистрированных заявок на блокировку карт"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)")
    })
    @GetMapping
    public ResponseEntity<List<CardBlockRequestResponse>> findAll(CardBlockRequestFilter filter) {
        return ResponseEntity.ok(service.findAll(filter));
    }

    @Operation(
            summary = "Получение заявки на блокировку карты по ID",
            description = "Возвращает полную информацию по заявке на блокировку карты"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CardBlockRequestResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Одобрить заявку на блокировку карты",
            description = "Выполняет блокировку карты пользователя и обновляет статус заявки"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена")
    })
    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(
            @AuthenticationPrincipal AuthUserDetails userDetails,
            @PathVariable Long id)
    {
        service.resolveRequest(id, userDetails.getId(), true);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Отклонить заявку на блокировку карты",
            description = "Отклоняет блокировку карты пользователя и обновляет статус заявки"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена")
    })
    @PostMapping("/{id}/deny")
    public ResponseEntity<Void> deny(
            @AuthenticationPrincipal AuthUserDetails userDetails,
            @PathVariable Long id)
    {
        service.resolveRequest(id, userDetails.getId(), false);

        return ResponseEntity.ok().build();
    }
}
