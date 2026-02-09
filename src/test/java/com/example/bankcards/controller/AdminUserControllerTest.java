package com.example.bankcards.controller;

import com.example.bankcards.AbstractTestController;
import com.example.bankcards.StringTestUtils;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserService;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminUserControllerTest extends AbstractTestController {

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"}
    )
    public void whenFindAllUsers_thenReturnUserList() throws Exception {
        UserResponse user1 = createUserResponse(1L, "ACTIVE");
        UserResponse user2 = createUserResponse(2L, "BLOCKED");

        CardResponse card = createCardResponse(1L, 2L, "69.00", "ACTIVE");
        user2.setCards(List.of(card));

        Mockito.when(userService.findAll(0, 10)).thenReturn(List.of(user1, user2));

        String expectedResponse = StringTestUtils.readStringFromResource("get_all_users_response.json");

        String actualResponse = mockMvc.perform(
                get("/users?page=0&limit=10")
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(userService, times(1)).findAll(0, 10);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"}
    )
    public void whenFindById_thenReturnUser() throws Exception {
        UserResponse user = createUserResponse(1L, "ACTIVE");

        CardResponse card = createCardResponse(1L, 1L, "69.00", "ACTIVE");
        user.setCards(List.of(card));

        Mockito.when(userService.findById(1L)).thenReturn(user);

        String expectedResponse = StringTestUtils.readStringFromResource("get_single_user_response.json");

        String actualResponse = mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(userService, times(1)).findById(1L);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"}
    )
    public void whenDeleteById_thenNoContent() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"}
    )
    public void whenActivateUser_thenCallActivate() throws Exception {
        mockMvc.perform(put("/users/1/activate"))
                .andExpect(status().isOk());

        Mockito.verify(userService, times(1)).activate(1L);
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"}
    )
    public void whenBlockUser_thenCallActivate() throws Exception {
        mockMvc.perform(put("/users/1/block"))
                .andExpect(status().isOk());

        Mockito.verify(userService, times(1)).block(1L);
    }
}
