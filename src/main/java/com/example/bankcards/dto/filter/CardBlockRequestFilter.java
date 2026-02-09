package com.example.bankcards.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Фильтрация поиска по заявкам на блокировку карт")
public class CardBlockRequestFilter {

    @Schema(example = "0")
    @PositiveOrZero(message = "Номер страницы должен быть 0 или больше")
    private Integer page = 0;

    @Schema(example = "10")
    @Positive(message = "Лимит не может быть меньше 1")
    private Integer limit = 10;

    @Schema(example = "PENDING")
    @Pattern(regexp = "^(PENDING|APPROVED|DENIED)$", message = "Статус может быть ACTIVE, APPROVED или DENIED")
    private String status;
}
