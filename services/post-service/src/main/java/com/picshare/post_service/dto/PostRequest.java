package com.picshare.post_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostRequest {

  private final Long userId;
  private final String description;
  private final String[] tags;
  
}
