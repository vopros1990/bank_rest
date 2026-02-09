package com.example.bankcards;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class AbstractTestController {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected UserResponse createUserResponse(Long id, String status) {
        return UserResponse.builder()
                .id(id)
                .roles(List.of("USER"))
                .status(status)
                .username("user" + id)
                .firstName("FirstName" + id)
                .lastName("LastName" + id)
                .email("user" + id + "@example.com")
                .cards(List.of())
                .build();
    }

    protected CardRequest createCardRequest(String number, Long holderId, String balance) {
        return CardRequest.builder()
                .number(number)
                .balance(balance)
                .holderId(holderId)
                .expiryMonth(10)
                .expiryYear(2026)
                .build();
    }

    protected CardResponse createCardResponse(Long id, Long holderId, String balance, String status) {
        return CardResponse.builder()
                .id(id)
                .number("**** **** **** 123" + id)
                .balance(balance)
                .holderId(holderId)
                .expiryMonth(10)
                .expiryYear(2026)
                .status(status)
                .build();
    }
}
