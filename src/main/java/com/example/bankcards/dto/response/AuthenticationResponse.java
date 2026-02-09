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
@Schema(description = "Ответ при успешной авторизации")
public class AuthenticationResponse {
    @Schema(example = "1")
    private Long id;

    @Schema(example = "user")
    private String username;

    @Schema(example = "[{USER}]")
    List<String> roles;

    @Schema(example = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTc3MDU5NTQ3MCwiZXhwIjoxNzcwNTk3MjcwfQ.EMWHBqhrCmB-8j8wI3P_kdLTyKp2UcocGB4d0K4KRr6vQ_sDHBiHY_d2QBjQNdbQ")
    private String token;
}
