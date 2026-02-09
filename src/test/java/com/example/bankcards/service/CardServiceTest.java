package com.example.bankcards.service;

import com.example.bankcards.AbstractTestService;
import com.example.bankcards.dto.filter.CardFilter;
import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

public class CardServiceTest extends AbstractTestService {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    @Test
    public void whenFindAll_thenReturnCardResponseList() {
        Card card = createCard(1L);
        CardResponse cardResponse = createCardResponse(1L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Card> page = new PageImpl<>(List.of(card), pageable, 1);
        CardFilter filter = new CardFilter(0, 10, null);

        Mockito.when(cardRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(page);
        Mockito.when(cardMapper.toResponse(card)).thenReturn(cardResponse);

        List<CardResponse> result = cardService.findAll(filter);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        Mockito.verify(cardRepository, times(1))
                .findAll(any(Specification.class), eq(pageable));
        Mockito.verify(cardMapper, times(1)).toResponse(card);
    }

    @Test
    public void whenFindAllCalledNoCardsExist_thenReturnEmptyList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Card> page = new PageImpl<>(List.of(), pageable, 1);
        CardFilter filter = new CardFilter(0, 10, null);

        Mockito.when(cardRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(page);

        List<CardResponse> result = cardService.findAll(filter);

        Mockito.verify(cardRepository, times(1))
                .findAll(any(Specification.class), eq(pageable));
        Mockito.verify(cardMapper, times(0)).toResponse(null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void whenFindByHolderId_thenReturnCardList() {
        Card card = createCard(1L);
        CardResponse cardResponse = createCardResponse(1L, 1L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Card> page = new PageImpl<>(List.of(card), pageable, 1);
        CardFilter filter = new CardFilter(0, 10, null);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        Mockito.when(cardRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(page);
        Mockito.when(cardMapper.toResponse(card)).thenReturn(cardResponse);

        List<CardResponse> result = cardService.findByHolderId(1L, filter);

        Mockito.verify(cardRepository, times(1))
                .findAll(any(Specification.class), eq(pageable));
        Mockito.verify(cardMapper, times(1)).toResponse(card);

        assertEquals(1L, result.get(0).getHolderId());
    }

    @Test
    public void whenFindById_thenReturnCard() {
        Card card = createCard(1L);
        CardResponse cardResponse = createCardResponse(1L);

        Mockito.when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        Mockito.when(cardMapper.toResponse(card)).thenReturn(cardResponse);

        CardResponse result = cardService.findById(1L);

        Mockito.verify(cardRepository, times(1)).findById(1L);
        Mockito.verify(cardMapper, times(1)).toResponse(card);

        assertEquals(result.getId(), 1L);
    }

    @Test
    public void givenNotExistsCardId_whenFindById_thenThrowException() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardService.findById(1L));

        Mockito.verify(cardRepository, times(1)).findById(1L);
        Mockito.verify(cardMapper, times(0)).toResponse(null);
    }

    @Test
    public void whenCreate_thenCreateCard() {
        ReflectionTestUtils.setField(cardService, "secret",
                Base64.getEncoder().encodeToString("test-secret".getBytes()));

        Card card = createCard(1L);
        CardResponse cardResponse = createCardResponse(1L);
        CardRequest cardRequest = createCardRequest("1234432256768736", 1L);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        Mockito.when(cardRepository.existsByNumber("1234567891456789")).thenReturn(false);
        Mockito.when(cardRepository.save(card)).thenReturn(card);
        Mockito.when(cardMapper.toEntity(cardRequest)).thenReturn(card);
        Mockito.when(cardMapper.toResponse(card)).thenReturn(cardResponse);

        CardResponse result = cardService.create(cardRequest);

        Mockito.verify(cardRepository, times(1)).save(card);
        Mockito.verify(cardMapper, times(1)).toResponse(card);
        Mockito.verify(cardMapper, times(1)).toEntity(cardRequest);

        assertEquals(1L, result.getId());
    }

    @Test
    public void whenUpdate_thenUpdateCard() {
        ReflectionTestUtils.setField(cardService, "secret",
                Base64.getEncoder().encodeToString("test-secret".getBytes()));

        Card card = createCard(1L);
        CardResponse cardResponse = createCardResponse(1L);
        CardRequest cardRequest = createCardRequest("1234432256768736", 1L);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        Mockito.when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        Mockito.when(cardRepository.existsByNumber("1234567891456789")).thenReturn(false);
        Mockito.when(cardRepository.save(card)).thenReturn(card);
        Mockito.doNothing().when(cardMapper).updateEntityFromRequest(cardRequest, card);
        Mockito.when(cardMapper.toResponse(card)).thenReturn(cardResponse);

        CardResponse result = cardService.update(cardRequest, 1L);

        Mockito.verify(cardRepository, times(1)).save(card);
        Mockito.verify(cardMapper, times(1)).toResponse(card);
        Mockito.verify(cardMapper, times(1)).updateEntityFromRequest(cardRequest, card);

        assertEquals(1L, result.getId());
    }

    @Test
    public void whenDeleteById_thenDeleteById() {
        Mockito.when(cardRepository.findById(1L)).thenReturn(Optional.of(new Card()));

        Mockito.doNothing().when(cardRepository).deleteById(1L);

        cardService.deleteById(1L);

        Mockito.verify(cardRepository, times(1)).deleteById(1L);
    }

    @Test
    public void whenActivate_thenActivateCard() {
        Card card = new Card();

        Mockito.when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        Mockito.when(cardRepository.save(card)).thenReturn(card);

        cardService.activate(1L);

        Mockito.verify(cardRepository, times(1)).save(card);
    }

    @Test
    public void whenBlock_thenBlockCard() {
        Card card = new Card();

        Mockito.when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        Mockito.when(cardRepository.save(card)).thenReturn(card);

        cardService.block(1L);

        Mockito.verify(cardRepository, times(1)).save(card);
    }

    @Test
    public void whenGetBalance_thenReturnBalance() {
        Card card = new Card();
        card.setBalance(new BigDecimal("69.00"));
        card.setHolder(createUser(1L, "test_user"));

        Mockito.when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        BalanceResponse result = cardService.getBalance(1L, 1L);

        Mockito.verify(cardRepository, times(1)).findById(1L);

        assertEquals("69.00", result.getBalance());
    }
}
