package com.picshare.userservice.service.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.picshare.userservice.service.dto.UserDTO;
import com.picshare.userservice.service.entity.UserEntity;
import com.picshare.userservice.service.repository.ConnectionRepository;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "followersCount", expression = "java((int) repo.countByFollowed(entity))")
  @Mapping(target = "followedCount", expression = "java((int) repo.countByFollower(entity))")
  UserDTO toDto(UserEntity entity, @Context ConnectionRepository repo);

  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "password", ignore = true)
  UserEntity toEntity(UserDTO dto);
}
