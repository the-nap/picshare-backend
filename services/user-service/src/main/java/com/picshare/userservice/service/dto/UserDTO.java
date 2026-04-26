package com.picshare.userservice.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

  @NotBlank(message = "Id cannot be blank")
  String id;

  @NotBlank(message = "username cannot be blank")
  String username;

  @Size(max=140)
  String bio;

  @Min(0)
  int followersCount;

  @Min(0)
  int followedCount;

}
