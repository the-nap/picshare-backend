package com.picshare.post_service.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateDto {

  @NotBlank(message = "userId cannot be blank")
  String userId;
  
  @NotBlank(message = "postId cannot be blank")
  String postId;

}
