package com.picshare.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Credential {

  private final String type;
  private final String value;
  
}

