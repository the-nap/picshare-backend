package com.picshare.userservice.mapper;

import org.mapstruct.Mapper;

import com.picshare.userservice.dto.UserDTO;
import com.picshare.userservice.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDTO toDto(UserEntity entity);
  UserEntity toEntity(UserDTO dto);
}
