package com.picshare.post_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageMetadata {

  private String userId;
  private String[] tags;
  private String caption;
  private String type;
  
}
