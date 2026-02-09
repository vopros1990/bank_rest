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
@Schema(description = "Ответ при ошибке авторизации")
public class AuthorizationErrorResponse {
    @Schema(example = "403")
    private Integer status;

    @Schema(example = "/path/to/resource")
    private String path;

    @Schema(example = "Доступ запрещен. Пользователь заблокирован")
    private String message;
}
