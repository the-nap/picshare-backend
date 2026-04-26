package com.picshare.userservice.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthUserDTO {
  
  @NotBlank(message = "Id cannot be blank")
  String id;

  @NotBlank(message = "Email cannot be blank")
  String email;

  @NotBlank(message = "Username cannot be blank")
  String username;

}
