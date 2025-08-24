package com.matchmeeat.role.mapper;

import com.matchmeeat.role.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    @Named("roleToName")
    default String roleToName(Role role) {
        return role.getAuthority();
    }
}
