package com.picshare.userservice.service.mapper;

import org.mapstruct.Mapper;

import com.picshare.userservice.service.dto.AuthUserDTO;
import com.picshare.userservice.service.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface AuthMapper{

  AuthUserDTO toDto(UserEntity entity);

  UserEntity toEntity(AuthUserDTO dto);
}
