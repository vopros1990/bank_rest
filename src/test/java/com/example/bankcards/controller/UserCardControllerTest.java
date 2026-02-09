package com.example.bankcards.controller;

import com.example.bankcards.AbstractTestController;
import com.example.bankcards.StringTestUtils;
import com.example.bankcards.dto.filter.CardFilter;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.*;
import com.example.bankcards.security.AuthUserDetails;
import com.example.bankcards.service.CardBlockRequestService;
import com.example.bankcards.service.CardService;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserCardControllerTest extends AbstractTestController {

    private AuthUserDetails principal;

    @MockitoBean
    private CardService cardService;

    @MockitoBean
    private CardBlockRequestService cardBlockRequestService;

    @BeforeEach
    public void setup() {
        principal = new AuthUserDetails(User.builder()
                .id(1L)
                .roles(Set.of(Role.from(RoleType.USER)))
                .username("test_user")
                .build());
    }

    @Test
    public void whenFindUserCards_returnCardsList() throws Exception {

        CardResponse card1 = createCardResponse(1L, 1L, "69.00", "ACTIVE");
        CardResponse card2 = createCardResponse(2L, 1L, "69.00", "BLOCKED");

        CardFilter filter = new CardFilter(0, 10, null);

        Mockito.when(cardService.findByHolderId(1L, filter)).thenReturn(List.of(card1, card2));

        String expectedResponse = StringTestUtils.readStringFromResource("get_all_user_cards_response.json");

        String actualResponse = mockMvc.perform(get("/users/me/cards")
                .with(authentication(new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities()))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(cardService, Mockito.times(1)).findByHolderId(1L, filter);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenGetCardBalance_returnUserCardBalance() throws Exception {
        BalanceResponse response = new BalanceResponse("69.00");

        Mockito.when(cardService.getBalance(1L, 1L)).thenReturn(response);

        String expectedResponse = StringTestUtils.readStringFromResource("get_balance_response.json");

        String actualResponse = mockMvc.perform(get("/cards/1/balance")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                principal, null, principal.getAuthorities()))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(cardService, Mockito.times(1)).getBalance(1L, 1L);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenSendCardBlockRequest_thenIsOk() throws Exception {
        mockMvc.perform(post("/cards/1/block-request")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                principal, null, principal.getAuthorities()))))
                .andExpect(status().isOk());

        Mockito.verify(cardBlockRequestService, Mockito.times(1)).addBlockRequest(1L, 1L);
    }
}
