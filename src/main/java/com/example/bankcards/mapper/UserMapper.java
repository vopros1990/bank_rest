package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.UserRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import org.mapstruct.*;

@Mapper(
        uses = { CardMapper.class, UserMapperUtils.class },
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequest request);

    @Mapping(target = "status", qualifiedByName = "mapStatus")
    @Mapping(target = "roles", qualifiedByName = "mapRoles")
    UserResponse toResponse(User user);
}
