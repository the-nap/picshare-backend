package com.picshare.post_service.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.picshare.post_service.dto.PostRequest;
import com.picshare.post_service.dto.PostResponse;
import com.picshare.post_service.entity.PostEntity;

@Mapper(componentModel = "spring")
public interface PostMapper {

  @Mapping(source = "tags", target = "tags", qualifiedByName = "listToString")
  PostResponse toDto(PostEntity entity);

  @Mapping(source = "tags", target = "tags", qualifiedByName = "stringToList")
  PostEntity toEntity(PostRequest dto);

  @Named("stringToList")
  public static List<String> stringToList(String string){
    List<String> result = new ArrayList<>();
    StringTokenizer st = new StringTokenizer(string);
    while(st.hasMoreTokens())
      result.add(st.nextToken());
    return result;
  }

  @Named("listToString")
  public static String listToString(List<String> tokens){
    StringBuilder sb = new StringBuilder();
    tokens.stream().forEach((token) -> 
        sb.append(token + ", "));
    return sb.toString();
  }
  
}
