package com.picshare.feed_service.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateDto {

  @NotBlank(message = "User Id cannot be blank")
  private String userId;

  @NotBlank(message = "Post Id cannot be blank")
  private String postId;
}
