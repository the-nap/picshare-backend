package com.picshare.post_service.mapper;

import org.mapstruct.Mapper;

import com.picshare.post_service.dto.ImageMetadata;
import com.picshare.post_service.entity.PostEntity;

@Mapper(componentModel = "spring")
public interface PostMapper {

  ImageMetadata toDto(PostEntity entity);

  PostEntity toEntity(ImageMetadata metadata);
  
}
