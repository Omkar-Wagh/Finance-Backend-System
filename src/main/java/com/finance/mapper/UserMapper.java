package com.finance.mapper;

import com.finance.dto.UserDto;
import com.finance.dto.UserRequestDto;
import com.finance.entity.User;
import com.finance.enums.Role;
import com.finance.exception.BadRequestException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "roleStringToEnum")
    User toEntity(UserRequestDto dto);

    @Mapping(target = "role", source = "role", qualifiedByName = "roleEnumToString")
    UserDto toDTO(User user);

    @Named("roleEnumToString")
    static String mapRoleToString(Role role) {
        return (role == null) ? null : role.name();
    }

    @Named("roleStringToEnum")
    static Role mapRoleToEnum(String role) {
        if (role == null || role.isBlank()) {
            throw new BadRequestException("Category is required");
        }
        try {
            return Role.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + role);
        }
    }
}
