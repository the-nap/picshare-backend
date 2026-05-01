package com.picshare.post_service.service.mapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.picshare.post_service.service.dto.PostRequest;
import com.picshare.post_service.service.dto.PostResponse;
import com.picshare.post_service.service.entity.PostEntity;
import com.picshare.post_service.service.entity.TagEntity;

@Mapper(componentModel = "spring")
public interface PostMapper {

  @Mapping(source = "tags", target = "tags", qualifiedByName = "setToString")
  PostResponse toDto(PostEntity entity);

  @Mapping(source = "tags", target = "tags", qualifiedByName = "stringToSet")
  PostEntity toEntity(PostRequest dto);

  @Named("stringToSet")
  public static Set<TagEntity> map(String string){

    if(string.isBlank())
      return Collections.emptySet();

    Set<TagEntity> result = new HashSet<>();
    StringTokenizer st = new StringTokenizer(string);
    while (st.hasMoreTokens()){
      TagEntity tag = new TagEntity(st.nextToken());
      result.add(tag);
    }
    return result;
  }

  @Named("setToString")
  public static String map(Set<TagEntity> tokens){
    if(tokens.isEmpty())
      return "";
    StringBuilder sb = new StringBuilder();
    tokens.stream().forEach((token) -> 
        sb.append(token.getTagName() + " "));
    return sb.toString();
  }
  
}
