package com.example.bankcards.controller;

import com.example.bankcards.dto.request.BalanceTransferRequest;
import com.example.bankcards.security.AuthUserDetails;
import com.example.bankcards.service.CardBalanceTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
@Tag(name = "Card balance (ALL REGISTERED USERS)", description = "Просмотр баланса личной карты")
@RequiredArgsConstructor
public class BalanceTransferController {

    private final CardBalanceTransferService service;

    @Operation(
            summary = "Перевод между личными картами",
            description = "Выполняет перевод между личными картами пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неправильно указаны параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена"),
            @ApiResponse(responseCode = "409", description = "Недостаточно денег на счету")
    })
    @PostMapping
    public ResponseEntity<Void> makeLocalBalanceTransfer(
            @RequestBody BalanceTransferRequest request,
            @AuthenticationPrincipal AuthUserDetails userDetails) {
        service.makeLocalTransfer(request, userDetails.getId());

        return ResponseEntity.ok().build();
    }
}
