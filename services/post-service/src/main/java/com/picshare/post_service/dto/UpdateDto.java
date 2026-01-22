package com.picshare.post_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateDto {

  private final Long postId;
  private final Long userId;

}
