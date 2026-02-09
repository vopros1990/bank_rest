package com.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на создание или редактирование карты")
public class CardRequest {
    @Schema(example = "2200765498762903")
    @Pattern(
            regexp = "^[1-9][0-9]{15}$",
            message = "Номер карты должен состоять из 16 цифр и не может начинаться с цифры 0")
    @NotNull(message = "Укажите номер карты")
    private String number;

    @Schema(example = "1")
    @NotNull(message = "Укажите ID владельца карты")
    @Positive(message = "ID владельца не может быть меньше 1")
    private Long holderId;

    @Schema(example = "5")
    @Range(min = 1, max= 12, message = "Месяц окончания срока действия карты не может быть меньше 1 или больше 12")
    @NotNull(message = "Укажите месяц окончания срока действия карты")
    private Integer expiryMonth;

    @Schema(example = "2026")
    @Range(min = 2000, max= 2050, message = "Год окончания срока действия карты не может быть меньше 2000 или больше 2050")
    @NotNull(message = "Укажите год окончания срока действия карты")
    private Integer expiryYear;

    @Schema(example = "1500.00")
    @Pattern(regexp = "^[0-9]{1,19}\\.[0-9]{2}$", message = "Укажите баланс карты в формате 0.00")
    private String balance = "0.00";
}
