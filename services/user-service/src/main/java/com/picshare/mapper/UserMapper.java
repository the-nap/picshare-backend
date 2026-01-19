package com.picshare.mapper;

import org.mapstruct.Mapper;

import com.picshare.dto.UserDTO;
import com.picshare.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDTO toDto(UserEntity entity);
  UserEntity toEntity(UserDTO dto);
}
