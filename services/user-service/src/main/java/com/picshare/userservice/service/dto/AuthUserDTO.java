package com.picshare.userservice.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthUserDTO {
  
  @NotBlank(message = "Id cannot be blank")
  String id;

  @NotBlank(message = "Email cannot be blank")
  String email;

  @NotBlank(message = "Username cannot be blank")
  String username;

}
