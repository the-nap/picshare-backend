package com.picshare.userservice.service.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CredentialDTO {

  private final String type;
  private final String value;
  
}
