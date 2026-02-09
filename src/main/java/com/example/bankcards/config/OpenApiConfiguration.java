package com.example.bankcards.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPIDescription() {
        Server localhostServer = new Server();

        localhostServer.setUrl("http://localhost:8080");
        localhostServer.setDescription("Локальный сервер");

        Contact contact = new Contact();
        contact.setName("Даниил Харчев");
        contact.setEmail("vopros-otvet1990@mail.ru");

        Info info = new Info()
                .title("Bank Cards API")
                .description("REST приложение для управления банковскими картами и локальными переводами")
                .contact(contact)
                .version("0.0.1-SNAPSHOT");

        return new OpenAPI()
                .info(info)
                .servers(List.of(localhostServer))
                .addSecurityItem(new SecurityRequirement().addList("JWT"));
    }
}
