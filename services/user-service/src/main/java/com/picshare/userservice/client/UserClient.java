package com.picshare.userservice.client;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserClient {

  private final RestClient restClient;

  public boolean uploadAvatar(MultipartFile image, String id){

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    HttpHeaders partHeaders = new HttpHeaders();
    partHeaders.setContentType(MediaType.parseMediaType(image.getContentType()));

    HttpEntity<Resource> part = new HttpEntity<>(image.getResource(), partHeaders);

    body.add("file", part);

    ResponseEntity<String> response = this.restClient
      .post()
      .uri(String.format("http://storage-service:8080/media/avatar/%s", id))
      .body(body)
      .contentType(MediaType.MULTIPART_FORM_DATA)
      .retrieve()
      .toEntity(String.class);
    
    if(response.getStatusCode().is2xxSuccessful())
      return true;
    return false;
  }

}

