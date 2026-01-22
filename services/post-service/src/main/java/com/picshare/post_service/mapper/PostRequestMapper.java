package com.picshare.post_service.mapper;

import org.mapstruct.Mapper;

import com.picshare.post_service.dto.PostRequest;
import com.picshare.post_service.entity.PostEntity;

@Mapper(componentModel = "spring")
public interface PostRequestMapper {

  PostRequest toDto(PostEntity entity);

  PostEntity toEntity(PostRequest dto);
  
}
