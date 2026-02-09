package com.example.bankcards.service;

import com.example.bankcards.dto.request.BalanceTransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.AccessDeniedDomainException;
import com.example.bankcards.exception.CardBalanceException;
import com.example.bankcards.exception.CardExpiredException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Service
@Validated
@RequiredArgsConstructor
public class CardBalanceTransferService {

    private final CardRepository cardRepository;

    private final UserRepository userRepository;

    public void makeLocalTransfer(
            @Valid BalanceTransferRequest request,
            @Positive(message = "ID пользователя не может быть меньше 1") Long userId) {
        Card fromCard = cardRepository.findById(request.getFromCardId())
                .orElseThrow(() -> new EntityNotFoundException("Карта отправителя не найдена"));

        Card toCard = cardRepository.findById(request.getToCardId())
                .orElseThrow(() -> new EntityNotFoundException("Карта получателя не найдена"));

        if (!fromCard.getHolder().getId().equals(userId))
            throw new AccessDeniedDomainException("Доступ к операции запрещен");

        if (fromCard.isExpired())
            throw new CardExpiredException("Невозможно совершить перевод, карта отправителя просрочена");

        if (toCard.isExpired())
            throw new CardExpiredException("Невозможно совершить перевод, карта получателя просрочена");

        if ("BLOCKED".equals(fromCard.getStatus().name()))
            throw new CardExpiredException("Невозможно совершить перевод, карта отправителя заблокирована");

        if ("BLOCKED".equals(toCard.getStatus().name()))
            throw new CardExpiredException("Невозможно совершить перевод, карта получателя заблокирована");

        performTransfer(fromCard, toCard, request.getAmount());
    }

    @Transactional
    public void performTransfer(Card from, Card to, String amountString) throws CardBalanceException {
        BigDecimal amount = new BigDecimal(amountString);

        if (from.getBalance().compareTo(amount) < 0)
            throw new CardBalanceException("Недостаточно средств для перевода");

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        cardRepository.save(from);
        cardRepository.save(to);
    }

}
