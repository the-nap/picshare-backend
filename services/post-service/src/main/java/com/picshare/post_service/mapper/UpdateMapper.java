package com.picshare.post_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.picshare.post_service.dto.UpdateDto;
import com.picshare.post_service.entity.UpdateEntity;

@Mapper(componentModel = "spring")
public interface UpdateMapper {

  @Mapping(source = "id.postId", target = "postId")
  @Mapping(source = "id.userId", target = "userId")
  UpdateDto toDto(UpdateEntity entity);
  
}
