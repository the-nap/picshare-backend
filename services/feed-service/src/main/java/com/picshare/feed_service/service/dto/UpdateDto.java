package com.picshare.feed_service.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateDto {

  private String userId;
  private String postId;
}
