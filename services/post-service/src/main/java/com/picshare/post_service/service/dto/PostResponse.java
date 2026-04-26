package com.picshare.post_service.service.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostResponse {

  @NotBlank(message = "Post id cannot be blank")
  String id;

  @NotBlank(message = "Poster id cannot be blank")
  String userId;

  @Nullable
  String description;

  @Nullable
  String tags;

}
