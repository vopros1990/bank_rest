package com.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Запрос на создание или редактирование пользователя")
public class UserRequest {

    @Schema(example = "site_user")
    @NotEmpty(message = "Укажите логин")
    @NotNull(message = "Укажите логин")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    private String username;

    @Schema(example = "Иван")
    @NotEmpty(message = "Укажите имя")
    @NotNull(message = "Укажите имя")
    @Size(max = 255, message = "Имя не может содержать более 255 символов")
    private String firstName;

    @Schema(example = "Иванов")
    @NotEmpty(message = "Укажите фамилию")
    @NotNull(message = "Укажите фамилию")
    @Size(max = 255, message = "Фамилия не может содержать более 255 символов")
    private String lastName;

    @Schema(example = "ivanov@example.com")
    @NotEmpty(message = "Укажите email пользователя")
    @NotNull(message = "Укажите email пользователя")
    @Size(min = 5, max = 255, message = "Email должен содержать от 5 до 255 символов")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Schema(example = "12345")
    @NotEmpty(message = "Укажите пароль")
    @NotNull(message = "Укажите пароль")
    @Size(min = 5, max = 255, message = "Длина пароля должна быть не менее 5 и не более 255 символов")
    private String password;
}
