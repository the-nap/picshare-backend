package com.picshare.feed_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedDto {
  private String userId;
  private Long postId;
}
