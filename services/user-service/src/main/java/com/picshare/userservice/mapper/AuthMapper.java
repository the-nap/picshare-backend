package com.picshare.userservice.mapper;

import org.mapstruct.Mapper;

import com.picshare.userservice.dto.AuthUserDTO;
import com.picshare.userservice.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface AuthMapper{

  AuthUserDTO toDto(UserEntity entity);

  UserEntity toEntity(AuthUserDTO dto);
}
