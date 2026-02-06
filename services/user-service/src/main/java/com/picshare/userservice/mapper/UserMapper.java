package com.picshare.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.picshare.userservice.dto.UserDTO;
import com.picshare.userservice.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDTO toDto(UserEntity entity);

  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  UserEntity toEntity(UserDTO dto);
}
