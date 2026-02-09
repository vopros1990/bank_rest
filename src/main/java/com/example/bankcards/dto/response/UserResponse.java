package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Ответ с информацией о пользователе")
public class UserResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "user")
    private String username;

    @Schema(example = "Иван")
    private String firstName;

    @Schema(example = "ИИванов")
    private String lastName;

    @Schema(example = "ivanov@example.com")
    private String email;

    @Schema(example = "ACTIVE")
    private String status;

    @Schema(example = "[{ 'USER' }]")
    private List<String> roles;

    private List<CardResponse> cards;
}
