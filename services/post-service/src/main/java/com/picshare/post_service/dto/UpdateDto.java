package com.picshare.post_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateDto {

  private final String userId;
  private final Long postId;

}
