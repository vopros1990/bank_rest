package com.example.bankcards.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Ответ при ошибке авторизации")
public class CardBlockRequestResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(exampleClasses = CardResponse.class)
    private CardResponse card;

    @Schema(example = "PENDING")
    private String status;

    @Schema(example = "143567972987")
    private Instant createdAt;

    @Schema(example = "143567972987")
    private Instant resolvedAt;

    @Schema(exampleClasses = UserResponse.class)
    private UserResponse resolvedBy;
}
