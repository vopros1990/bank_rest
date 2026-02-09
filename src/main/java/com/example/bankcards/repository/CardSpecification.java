package com.example.bankcards.repository;

import com.example.bankcards.dto.filter.CardFilter;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.YearMonth;

public interface CardSpecification {
    static Specification<Card> withFilter(CardFilter filter) {
        return Specification.allOf(byStatus(filter.getStatus()));
    }

    static Specification<Card> byHolder(Long holderId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("holder").get("id"), holderId);
    }

    static Specification<Card> byStatus(String status) {
        if (status == null)
            return null;

        return (root, query, criteriaBuilder) -> {
            if (status.equals("EXPIRED")) {
                YearMonth now = YearMonth.now();

                return criteriaBuilder.or(criteriaBuilder.greaterThan(root.get("expiryYear"), now.getYear()),
                        criteriaBuilder.and(
                                criteriaBuilder.equal(root.get("expiryYear"), now.getYear()),
                                criteriaBuilder.greaterThan(root.get("expiryMonth"), now.getMonth().getValue())
                        ));
            }

            return criteriaBuilder.equal(root.get("status"), CardStatus.valueOf(status));
        };
    }
}
