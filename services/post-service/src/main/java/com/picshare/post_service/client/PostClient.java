package com.picshare.post_service.client;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.picshare.post_service.service.exceptions.ClientErrorException;
import com.picshare.post_service.service.exceptions.ExternalException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostClient {

  private final RestClient restClient;

  public String upload(InputStream data, String id) throws ExternalException, ClientErrorException, IOException{
    byte[] bytes = data.readAllBytes();

    ByteArrayResource image = new ByteArrayResource(bytes);

    ResponseEntity<String> response = restClient
      .post()
      .uri(String.format("http://storage-service:8080/media/%s", id))
      .body(image)
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
