package com.picshare.post_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponse {

  private String id;
  private String userId;
  private String description;
  private String tags;

}
