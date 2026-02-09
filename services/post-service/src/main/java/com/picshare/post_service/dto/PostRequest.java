package com.picshare.post_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostRequest {

  private final String userId;
  private final String description;
  private final String[] tags;
  
}
