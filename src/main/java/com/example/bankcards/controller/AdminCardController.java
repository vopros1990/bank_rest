package com.example.bankcards.controller;

import com.example.bankcards.dto.filter.CardFilter;
import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Cards (ADMIN)", description = "Операции с картами. Только для администраторов")
@RequiredArgsConstructor
public class AdminCardController {

    private final CardService service;

    @Operation(
            summary = "Список всех карт",
            description = "Возвращает список зарегистрированных карт"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)")
    })
    @GetMapping
    public ResponseEntity<List<CardResponse>> findAll(CardFilter filter) {
        return ResponseEntity.ok(service.findAll(filter));
    }

    @Operation(
            summary = "Получение карты по ID",
            description = "Возвращает полную информацию о карте"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Создание новой карты",
            description = "Возвращает полную информацию о созданной карте"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена"),
            @ApiResponse(responseCode = "409", description = "Карта уже существует")
    })
    @PostMapping
    public ResponseEntity<CardResponse> create(@RequestBody CardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(
            summary = "Редактирование существующей карты",
            description = "Возвращает полную информацию об отредактированной карте"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена"),
            @ApiResponse(responseCode = "409", description = "Карта уже существует")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> update(@RequestBody CardRequest request, @PathVariable long id) {
        return ResponseEntity.ok(service.update(request, id));
    }

    @Operation(
            summary = "Удаление карты",
            description = "Удаляет карту по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Карта удалена"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Активация карты",
            description = "Обновляет статус карты на ACTIVE"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена"),
    })
    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable long id) {
        service.activate(id);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Блокировка карты",
            description = "Обновляет статус карты на BLOCKED"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (разрешен только администратору)"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена"),
    })
    @PutMapping("/{id}/block")
    public ResponseEntity<Void> block(@PathVariable long id) {
        service.block(id);

        return ResponseEntity.ok().build();
    }
}
