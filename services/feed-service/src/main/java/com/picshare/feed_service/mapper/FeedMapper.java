package com.picshare.feed_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.picshare.feed_service.DTO.FeedDto;
import com.picshare.feed_service.entity.FeedEntity;

@Mapper(componentModel = "spring")
public interface FeedMapper {

  FeedDto toDto(FeedEntity feed);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "timestamp", ignore = true)
  FeedEntity toEntity(FeedDto feed);

}
