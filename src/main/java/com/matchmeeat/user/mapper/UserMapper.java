package com.matchmeeat.user.mapper;

import com.matchmeeat.role.mapper.RoleMapper;
import com.matchmeeat.user.dto.UserDto;
import com.matchmeeat.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = RoleMapper.class)
public interface UserMapper {

    @Mapping(source = "roles", target = "roles", qualifiedByName = "roleToName")
    UserDto userToUserDto(User user);
}
