package com.example.bankcards.service;

import com.example.bankcards.AbstractTestService;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

public class UserServiceTest extends AbstractTestService {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void whenFindAll_returnUserResponseList() {
        User user = createUser(1L, "user1");
        UserResponse userResponse = createUserResponse(1L, "user1");

        Pageable pageable = PageRequest.of(0,10);
        Page<User> page = new PageImpl<>(List.of(user), pageable, 1);

        Mockito.when(userRepository.findAll(pageable)).thenReturn(page);
        Mockito.when(userMapper.toResponse(user)).thenReturn(userResponse);

        List<UserResponse> result = userService.findAll(0,10);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        Mockito.verify(userRepository, times(1)).findAll(pageable);
        Mockito.verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    public void whenFindAllCalledNoUsersExist_thenReturnEmptyList() {
        Pageable pageable = PageRequest.of(0,10);
        Page<User> page = new PageImpl<>(List.of(), pageable, 0);

        Mockito.when(userRepository.findAll(pageable)).thenReturn(page);

        List<UserResponse> result = userService.findAll(0, 10);

        Mockito.verify(userRepository, times(1)).findAll(pageable);
        Mockito.verify(userMapper, times(0)).toResponse(null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void whenFindById_thenReturnUser() {
        User user = createUser(1L, "user1");
        UserResponse userResponse = createUserResponse(1L, "user1");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.findById(1L);

        Mockito.verify(userRepository, times(1)).findById(1L);
        Mockito.verify(userMapper, times(1)).toResponse(user);

        assertEquals(result.getId(), 1L);
    }

    @Test
    public void givenNotExistsUserId_whenFindById_thenThrowException() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(1L));

        Mockito.verify(userRepository, times(1)).findById(1L);
        Mockito.verify(userMapper, times(0)).toResponse(null);
    }

    @Test
    public void whenDeleteById_thenDeleteById() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        Mockito.doNothing().when(userRepository).deleteById(1L);

        userService.deleteById(1L);

        Mockito.verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void whenActivate_thenActivateUser() {
        User user = new User();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        userService.activate(1L);

        Mockito.verify(userRepository, times(1)).save(user);
    }

    @Test
    public void whenBlock_thenBlockUser() {
        User user = new User();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        userService.block(1L);

        Mockito.verify(userRepository, times(1)).save(user);
    }
}
