package com.picshare.userservice.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.picshare.userservice.dto.UserDTO;
import com.picshare.userservice.entity.UserEntity;
import com.picshare.userservice.repository.ConnectionRepository;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "followersCount", expression = "java((int) repo.countByFollower(entity))")
  @Mapping(target = "followedCount", expression = "java((int) repo.countByFollowed(entity))")
  UserDTO toDto(UserEntity entity, @Context ConnectionRepository repo);

  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "password", ignore = true)
  UserEntity toEntity(UserDTO dto);
}
