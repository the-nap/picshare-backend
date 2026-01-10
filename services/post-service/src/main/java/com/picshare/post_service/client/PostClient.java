package com.picshare.post_service.client;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PostClient {

  private final RestClient restClient;

  public boolean upload(InputStream data, String id) {
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

    parts.add("file", new InputStreamResource(data));
    parts.add("id", id);

    ResponseEntity<Void> response = restClient
      .post()
      .uri("http://storage-service:8080")
      .body(parts)
      .retrieve()
      .toBodilessEntity();

    return response.getStatusCode().is2xxSuccessful();

  }

}
