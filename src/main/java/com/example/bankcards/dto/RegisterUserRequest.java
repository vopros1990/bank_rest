package com.example.bankcards.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {
    @NotEmpty(message = "Укажите имя пользователя")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    private String username;

    @NotEmpty(message = "Укажите email пользователя")
    @Size(min = 5, max = 255, message = "Email должен содержать от 5 до 255 символов")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @NotEmpty(message = "Укажите пароль")
    @Size(min = 8, max = 255, message = "Длина пароля должна быть не менее 8 и не более 255 символов")
    private String password;
}
