package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Ответ с информацией о карте")
public class CardResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "**** **** **** 1234")
    private String number;

    @Schema(example = "200.65")
    private String balance;

    @Schema(example = "2")
    private Long holderId;

    @Schema(example = "ACTIVE")
    private String status;

    @Schema(example = "10")
    private Integer expiryMonth;

    @Schema(example = "2026")
    private Integer expiryYear;
}
