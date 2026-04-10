package com.picshare.userservice.client;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserClient {

  private final RestClient restClient;

  public boolean uploadAvatar(InputStream data, String id){
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("avatar", new InputStreamResource(data){
      @Override
      public String getFilename() {
        return "uploaded-image";
      }
    });

    ResponseEntity<String> response = this.restClient
      .post()
      .uri(String.format("http://storage-service:8080/%s", id))
      .body(body)
      .contentType(MediaType.MULTIPART_FORM_DATA)
      .retrieve()
      .toEntity(String.class);
    
    if(response.getStatusCode().is2xxSuccessful())
      return true;
    return false;
  }

}

