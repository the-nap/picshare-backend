package com.picshare.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedDto {
  private Long userId,postId;
}
