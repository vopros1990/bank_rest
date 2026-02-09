package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CardBlockRequestRepository extends
        JpaRepository<CardBlockRequest, Long>,
        PagingAndSortingRepository<CardBlockRequest, Long>,
        JpaSpecificationExecutor<CardBlockRequest> {

    Boolean existsByCard(Card card);
}
