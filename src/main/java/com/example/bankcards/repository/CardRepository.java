package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CardRepository extends
        JpaRepository<Card, Long>,
        PagingAndSortingRepository<Card, Long>,
        JpaSpecificationExecutor<Card> {

    Boolean existsByNumber(String number);
}
