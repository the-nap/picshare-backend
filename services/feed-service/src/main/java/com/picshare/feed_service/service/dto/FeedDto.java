package com.picshare.feed_service.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedDto {
  
  @NotBlank(message = "User Id cannot be blank")
  String userId;

  @NotBlank(message = "Post Id cannot be blank")
  String postId;
}
