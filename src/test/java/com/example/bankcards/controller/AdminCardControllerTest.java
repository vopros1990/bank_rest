package com.example.bankcards.controller;

import com.example.bankcards.AbstractTestController;
import com.example.bankcards.StringTestUtils;
import com.example.bankcards.dto.filter.CardFilter;
import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.CardService;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminCardControllerTest extends AbstractTestController {
    @MockitoBean
    private CardService cardService;

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"}
    )
    public void whenFindAllCards_thenReturnCardList() throws Exception {
        CardResponse card1 = createCardResponse(1L, 1L, "69.00", "ACTIVE");
        CardResponse card2 = createCardResponse(2L, 2L, "69.00", "BLOCKED");

        CardFilter filter = new CardFilter(0, 10, null);

        Mockito.when(cardService.findAll(filter)).thenReturn(List.of(card1, card2));

        String expectedResponse = StringTestUtils.readStringFromResource("get_all_cards_response.json");

        String actualResponse = mockMvc.perform(get("/cards"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(cardService, times(1)).findAll(filter);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"}
    )
    public void whenFindById_thenReturnUser() throws Exception {
        CardResponse card = createCardResponse(1L, 1L, "69.00", "ACTIVE");

        Mockito.when(cardService.findById(1L)).thenReturn(card);

        String expectedResponse = StringTestUtils.readStringFromResource("get_single_card_response.json");

        String actualResponse = mockMvc.perform(get("/cards/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(cardService, times(1)).findById(1L);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"}
    )
    public void whenCreateCard_thenReturnCreatedCard() throws Exception {
        CardRequest request = createCardRequest("1234432112345671", 1L, "69.00");

        CardResponse response = createCardResponse(1L, 1L, "69.00", "ACTIVE");

        Mockito.when(cardService.create(request)).thenReturn(response);

        String expectedResponse = StringTestUtils.readStringFromResource("create_card_response.json");

        String actualResponse = mockMvc.perform(
                post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getContentAsString();

        Mockito.verify(cardService, times(1)).create(request);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"}
    )
    public void whenUpdateCard_thenReturnUpdatedCard() throws Exception {
        CardRequest request = createCardRequest("1234432112345671", 1L, "69.00");

        CardResponse response = createCardResponse(1L, 1L, "69.00", "ACTIVE");

        Mockito.when(cardService.update(request, 1L)).thenReturn(response);

        String expectedResponse = StringTestUtils.readStringFromResource("create_card_response.json");

        String actualResponse = mockMvc.perform(
                        put("/cards/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        Mockito.verify(cardService, times(1)).update(request, 1L);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"}
    )
    public void whenDeleteById_thenNoContent() throws Exception {
        mockMvc.perform(delete("/cards/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(cardService, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"}
    )
    public void whenActivateCard_thenCallActivate() throws Exception {
        mockMvc.perform(put("/cards/1/activate"))
                .andExpect(status().isOk());

        Mockito.verify(cardService, times(1)).activate(1L);
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"}
    )
    public void whenBlockCard_thenCallActivate() throws Exception {
        mockMvc.perform(put("/cards/1/block"))
                .andExpect(status().isOk());

        Mockito.verify(cardService, times(1)).block(1L);
    }
}
