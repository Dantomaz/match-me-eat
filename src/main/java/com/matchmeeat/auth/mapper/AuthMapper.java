package com.matchmeeat.auth.mapper;

import com.matchmeeat.auth.dto.RegistrationResponseDto;
import com.matchmeeat.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    RegistrationResponseDto userToRegistrationResponseDto(User user);
}
