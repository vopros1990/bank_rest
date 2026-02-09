package com.example.bankcards;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.RoleType;
import com.example.bankcards.entity.User;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class AbstractTestService {

    protected User createUser(Long id, String username) {
        return User.builder()
                .id(id)
                .username(username)
                .roles(Set.of(Role.from(RoleType.USER)))
                .build();
    }

    protected UserResponse createUserResponse(Long id, String username) {
        return UserResponse.builder()
                .id(id)
                .username(username)
                .build();
    }

    protected CardRequest createCardRequest(String number, Long holderId) {
        return CardRequest.builder()
                .number(number)
                .holderId(holderId)
                .build();
    }

    protected Card createCard(Long id) {
        return Card.builder()
                .id(id)
                .build();
    }

    protected CardResponse createCardResponse(Long id) {
        return CardResponse.builder()
                .id(id)
                .build();
    }

    protected CardResponse createCardResponse(Long id, Long holderId) {
        return CardResponse.builder()
                .id(id)
                .holderId(holderId)
                .build();
    }
}
