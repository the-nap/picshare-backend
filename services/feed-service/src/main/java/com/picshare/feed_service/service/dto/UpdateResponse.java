package com.picshare.feed_service.service.dto;

import java.util.Map;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateResponse {

  @NotNull
  Map<String, String> posts;

}
