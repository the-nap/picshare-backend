package com.picshare;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PicshareUser {
  private String id;
  private String email;
  private String username;
}
