package com.picshare.feed_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateDto {

  private String userId;
  private String postId;
}
