package com.example.bankcards.mapper;

import com.example.bankcards.dto.response.CardBlockRequestResponse;
import com.example.bankcards.entity.CardBlockRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        uses = {CardMapper.class, UserMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CardBlockRequestMapper {

    CardBlockRequestResponse toResponse(CardBlockRequest entity);

    List<CardBlockRequestResponse> toResponseList(List<CardBlockRequest> entityList);
}
