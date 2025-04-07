package com.sample.crm.mapper;

import com.sample.crm.dto.UserDTO;
import com.sample.crm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {
    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);

    @Mapping(target = "clients", ignore = true)
    void update(@MappingTarget User user, UserDTO userDTO);
}
