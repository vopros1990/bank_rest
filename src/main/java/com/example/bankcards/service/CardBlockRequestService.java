package com.example.bankcards.service;

import com.example.bankcards.dto.filter.CardBlockRequestFilter;
import com.example.bankcards.dto.response.CardBlockRequestResponse;
import com.example.bankcards.entity.*;
import com.example.bankcards.exception.AccessDeniedDomainException;
import com.example.bankcards.exception.AlreadyExistsException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.CardBlockRequestMapper;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardBlockRequestSpecification;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class CardBlockRequestService {

    private final CardBlockRequestMapper mapper;

    private final CardBlockRequestRepository cardBlockRequestRepository;

    private final CardRepository cardRepository;

    private final UserRepository userRepository;

    public List<CardBlockRequestResponse> findAll(@Valid CardBlockRequestFilter filter) {
        return cardBlockRequestRepository.findAll(
                        CardBlockRequestSpecification.withFilter(filter),
                        PageRequest.of(filter.getPage(), filter.getLimit()))
                .map(mapper::toResponse)
                .toList();
    }

    public CardBlockRequestResponse findById(
            @Positive(message = "ID заявки на блокировку карты должен быть больше 0") Long id)
    {
        return mapper.toResponse(cardBlockRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заявка на блокировку карты не найдена")));
    }

    @Transactional
    public void resolveRequest(
            @Positive(message = "ID заявки на блокировку карты должен быть больше 0") Long requestId,
            Long resolverId,
            boolean approve)
    {
        User resolvedBy = userRepository.findById(resolverId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с указанным ID не зарегистрирован"));

        CardBlockRequest cardBlockRequest = cardBlockRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Заявка на блокировку карты с указанным ID не найдена"));

        if (approve) {
            cardBlockRequest.getCard().setStatus(CardStatus.BLOCKED);
            cardBlockRequest.setStatus(CardBlockRequestStatus.APPROVED);
        } else {
            cardBlockRequest.setStatus(CardBlockRequestStatus.DENIED);
        }

        cardBlockRequest.setResolvedAt(Instant.now());
        cardBlockRequest.setResolvedBy(resolvedBy);

        cardBlockRequestRepository.save(cardBlockRequest);
    }

    public void addBlockRequest(
            @Positive(message = "ID пользователя не может быть меньше 1") Long holderId,
            @Positive(message = "ID карты не может быть меньше 1") Long cardId)
    {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта с указанным ID не найдена"));

        if (!card.getHolder().getId().equals(holderId))
            throw new AccessDeniedDomainException("Доступ запрещен");

        if (cardBlockRequestRepository.existsByCard(card))
            throw new AlreadyExistsException("Заявка на блокировку данной карты уже существует");

        CardBlockRequest cardBlockRequest = CardBlockRequest.builder()
                .card(card)
                .createdAt(Instant.now())
                .build();

        cardBlockRequestRepository.save(cardBlockRequest);
    }
}
