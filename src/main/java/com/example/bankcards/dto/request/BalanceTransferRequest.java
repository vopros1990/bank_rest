package com.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на перевод с карты на карту")
public class BalanceTransferRequest {

    @Schema(example = "1")
    @NotNull(message = "Укажите ID карты, с которой нужно отправить перевод")
    private Long fromCardId;

    @Schema(example = "2")
    @NotNull(message = "Укажите ID карты, на которую нужно отправить перевод")
    private Long toCardId;

    @Schema(example = "154.00")
    @NotNull(message = "Укажите сумму перевода")
    @NotEmpty(message = "Укажите сумму перевода")
    @Pattern(regexp = "^[0-9]{1,19}\\.[0-9]{2}$", message = "Укажите сумму перевода как строку в формате 0.00")
    private String amount;
}
