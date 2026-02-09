package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import org.mapstruct.*;

@Mapper(
        uses = CardMapperUtils.class,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CardMapper {
    @Mapping(target = "holder", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "last4", source = "number", qualifiedByName = "extractLast4")
    @Mapping(target = "balance", qualifiedByName = "mapEntityBalance")
    Card toEntity(CardRequest request);

    @Mapping(target = "number", source = "last4", qualifiedByName = "maskCardNumber")
    @Mapping(target = "holderId", source = "holder.id")
    @Mapping(target = "status", source = "card", qualifiedByName = "mapResponseStatus")
    CardResponse toResponse(Card card);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "holder", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "last4", source = "number", qualifiedByName = "extractLast4")
    @Mapping(target = "balance", qualifiedByName = "mapEntityBalance")
    void updateEntityFromRequest(CardRequest request, @MappingTarget Card target);
}
