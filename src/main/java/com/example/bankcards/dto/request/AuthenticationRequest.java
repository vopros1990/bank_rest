package com.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос для аутентификации")
public class AuthenticationRequest {

    @Schema(example = "site-user")
    @NotEmpty(message = "Укажите имя пользователя")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    private String username;

    @Schema(example = "12345")
    @NotEmpty(message = "Укажите пароль")
    @Size(min = 5, max = 255, message = "Длина пароля должна быть не менее 5 и не более 255 символов")
    private String password;
}
