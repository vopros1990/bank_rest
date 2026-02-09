package com.example.bankcards.controller;

import com.example.bankcards.dto.filter.CardFilter;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.security.AuthUserDetails;
import com.example.bankcards.service.CardBlockRequestService;
import com.example.bankcards.service.CardService;
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
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Cards (USER)", description = "Операции с личными картами")
@RequiredArgsConstructor
public class UserCardController {

    private final CardService cardService;

    private final CardBlockRequestService cardBlockRequestService;

    @Operation(
            summary = "Список карт пользователя",
            description = "Возвращает список карт пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
    })
    @GetMapping("/users/me/cards")
    public ResponseEntity<List<CardResponse>> getUserCards(
            @AuthenticationPrincipal AuthUserDetails userDetails,
            CardFilter filter) {
        return ResponseEntity.ok(cardService.findByHolderId(userDetails.getId(), filter));
    }

    @Operation(
            summary = "Запрос баланса карты пользователя",
            description = "Возвращает текущий баланс карты пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @GetMapping("/cards/{id}/balance")
    public ResponseEntity<BalanceResponse> getBalance(
            @AuthenticationPrincipal AuthUserDetails userDetails,
            @PathVariable long id) {
        return ResponseEntity.ok(cardService.getBalance(userDetails.getId(), id));
    }

    @Operation(
            summary = "Заявка на блокировку карты пользователя",
            description = "Отправляет заявку на блокировку карты пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @PostMapping("/cards/{id}/block-request")
    public ResponseEntity<Void> addBlockRequest(
            @AuthenticationPrincipal AuthUserDetails userDetails,
            @PathVariable long id) {
        cardBlockRequestService.addBlockRequest(userDetails.getId(), id);

        return ResponseEntity.ok().build();
    }
}
