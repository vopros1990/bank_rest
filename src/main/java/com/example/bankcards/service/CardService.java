package com.example.bankcards.service;

import com.example.bankcards.dto.filter.CardFilter;
import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AccessDeniedDomainException;
import com.example.bankcards.exception.AlreadyExistsException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardSpecification;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardNumberHasher;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class CardService {

    @Value("${app.cardNumberHasher.secretKey}")
    private String secret;

    private final CardMapper mapper;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public List<CardResponse> findAll(
            @Valid CardFilter filter)
    {
        return cardRepository
                .findAll(
                        CardSpecification.withFilter(filter),
                        PageRequest.of(filter.getPage(), filter.getLimit()))
                .map(mapper::toResponse)
                .toList();
    }

    public List<CardResponse> findByHolderId(
            @Positive(message = "ID карты не может быть меньше 1") Long holderId,
            @Valid CardFilter filter)
    {
        userRepository.findById(holderId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с указанным ID не зарегистрирован"));

        return cardRepository
                .findAll(
                        CardSpecification.withFilter(filter).and(CardSpecification.byHolder(holderId)),
                        PageRequest.of(filter.getPage(), filter.getLimit()))
                .map(mapper::toResponse)
                .toList();
    }

    public CardResponse findById(
            @Positive(message = "ID карты не может быть меньше 1") Long cardId)
    {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта с указанным ID не найдена"));

        return mapper.toResponse(card);
    }

    @Transactional
    public CardResponse create(@Valid CardRequest request) {
        User holder = userRepository.findById(request.getHolderId())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с указанным ID не зарегистрирован"));

        String cardNumberHash = CardNumberHasher.hash(request.getNumber(), secret);

        if (cardRepository.existsByNumber(cardNumberHash))
            throw new AlreadyExistsException("Карта уже существует");

        Card card = mapper.toEntity(request);

        card.setHolder(holder);
        card.setNumber(cardNumberHash);

        return mapper.toResponse(cardRepository.save(card));
    }

    @Transactional
    public CardResponse update(
            @Valid CardRequest request,
            @Positive(message = "ID карты не может быть меньше 1") Long cardId)
    {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта с указанным ID не найдена"));

        User holder = userRepository.findById(request.getHolderId())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с указанным ID не зарегистрирован"));

        String cardNumberHash = CardNumberHasher.hash(request.getNumber(), secret);

        if (cardRepository.existsByNumber(cardNumberHash))
            throw new AlreadyExistsException("Карта уже существует");

        mapper.updateEntityFromRequest(request, card);

        card.setHolder(holder);
        card.setNumber(cardNumberHash);

        return mapper.toResponse(cardRepository.save(card));
    }

    public void delete(@Positive(message = "ID карты не может быть меньше 1") Long cardId) {
        cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта с указанным ID не найдена"));

        cardRepository.deleteById(cardId);
    }

    @Transactional
    public void activate(@Positive(message = "ID карты не может быть меньше 1") Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта с указанным ID не найдена"));

        card.setStatus(CardStatus.ACTIVE);
    }

    @Transactional
    public void block(@Positive(message = "ID карты не может быть меньше 1") Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта с указанным ID не найдена"));

        card.setStatus(CardStatus.BLOCKED);
    }

    public BalanceResponse getBalance(
            @Positive(message = "ID пользователя не может быть меньше 1") Long holderId,
            @Positive(message = "ID карты не может быть меньше 1") Long cardId)
    {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта с указанным ID не найдена"));

        if (!card.getHolder().getId().equals(holderId))
            throw new AccessDeniedDomainException("Доступ запрещен");

        return new BalanceResponse(card.getBalance().toPlainString());
    }

}
