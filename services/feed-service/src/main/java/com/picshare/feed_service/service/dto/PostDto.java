package com.picshare.feed_service.service.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostDto {

  @NotBlank(message = "Id cannot be blank")
  private String id;
  
  @NotBlank(message = "User id cannot be blank")
  private String userId;

  @Size(max = 140, message = "Max description length is 140 characters")
  @Nullable
  private String description;

  @Nullable
  @Size(max = 25, message = "Tags max length is 25 characters (spaces included)")
  String tags;
  
}
