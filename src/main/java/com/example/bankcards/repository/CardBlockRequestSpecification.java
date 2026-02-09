package com.example.bankcards.repository;

import com.example.bankcards.dto.filter.CardBlockRequestFilter;
import com.example.bankcards.entity.CardBlockRequest;
import org.springframework.data.jpa.domain.Specification;

public interface CardBlockRequestSpecification {
    static Specification<CardBlockRequest> withFilter(CardBlockRequestFilter filter) {
        return Specification.allOf(byStatus(filter.getStatus()));
    }

    static Specification<CardBlockRequest> byStatus(String status) {
        if (status == null)
            return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status").get("name"), status);
    }
}
