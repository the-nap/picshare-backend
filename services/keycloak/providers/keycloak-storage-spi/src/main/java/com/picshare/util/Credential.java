package com.picshare.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Credential {

  private final String id;
  private final String password;
  
}

