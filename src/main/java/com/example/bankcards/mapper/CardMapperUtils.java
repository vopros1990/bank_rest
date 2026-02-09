package com.example.bankcards.mapper;

import com.example.bankcards.entity.Card;
import com.example.bankcards.util.CardMasker;
import lombok.NoArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@NoArgsConstructor
public class CardMapperUtils {

    @Named("extractLast4")
    public String extractLast4(String number) {
        return number.substring(number.length() - 4);
    }

    @Named("maskCardNumber")
    public String maskCardNumber(String last4) {
        return CardMasker.mask(last4);
    }

    @Named("mapEntityBalance")
    public BigDecimal mapEntityBalance(String balance) {
        return new BigDecimal(balance);
    }

    @Named("mapResponseStatus")
    public String mapResponseStatus(Card card) {
        if (card.isExpired()) return "EXPIRED";

        return card.getStatus().name();
    }
}
