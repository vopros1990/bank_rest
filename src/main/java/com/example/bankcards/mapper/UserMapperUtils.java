package com.example.bankcards.mapper;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.UserStatus;
import lombok.NoArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@NoArgsConstructor
public class UserMapperUtils {

    @Named("mapRoles")
    public List<String> mapRoles(Set<Role> roles) {
        List<String> mappedRoles = new ArrayList<>();

        for (Role role : roles) {
            mappedRoles.add(role.getRole().name());
        }

        return mappedRoles;
    }

    @Named("mapStatus")
    public String mapStatus(UserStatus status) {
        return status.name();
    }
}
