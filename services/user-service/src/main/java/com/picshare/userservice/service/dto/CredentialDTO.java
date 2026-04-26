package com.picshare.userservice.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CredentialDTO {

  @NotBlank(message = "Credential type not specified")
  final String type;

  @NotBlank(message = "Credential value cannot be blank")
  final String value;
  
}
