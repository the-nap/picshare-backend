package com.picshare.feed_service.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostDto {

  private Long id;
  private String userId;
  private String description;
  private String url;
  private List<String> tags;
  
}
