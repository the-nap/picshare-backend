package com.picshare.post_service.mapper;

import org.mapstruct.Mapper;

import com.picshare.post_service.dto.PostResponse;
import com.picshare.post_service.entity.PostEntity;

@Mapper(componentModel = "spring")
public interface PostResponseMapper {

  PostResponse toDto(PostEntity entity);

  PostEntity toEntity(PostResponse dto);
  
}
