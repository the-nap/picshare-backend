package com.picshare.post_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PostResponse {

  private Long id;
  private Long userId;
  private String description;
  private String url;
  private List<String> tags;

}
