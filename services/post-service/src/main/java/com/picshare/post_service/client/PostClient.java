package com.picshare.post_service.client;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.picshare.post_service.service.exceptions.ClientErrorException;
import com.picshare.post_service.service.exceptions.ExternalException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostClient {

  private final RestClient restClient;

  public String upload(InputStream data, Long id) throws ExternalException, ClientErrorException{
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

    parts.add("image", new InputStreamResource(data));
    parts.add("id", id);

    ResponseEntity<String> response = restClient
      .post()
      .uri("http://storage-service:8080")
      .header("Content-Type", "multipart/form-data")
      .body(parts)
      .retrieve()
      .toEntity(String.class);

    if(response.getStatusCode().is2xxSuccessful())
      return response.getBody();
    if(response.getStatusCode().is5xxServerError())
      throw new ExternalException(response.getBody());
    if(response.getStatusCode().is4xxClientError())
      throw new ClientErrorException(response.getBody());
    return "";
  }
}
