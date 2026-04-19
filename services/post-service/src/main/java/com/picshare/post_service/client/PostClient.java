package com.picshare.post_service.client;

import java.io.IOException;

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

import com.picshare.post_service.service.exceptions.ClientErrorException;
import com.picshare.post_service.service.exceptions.ExternalException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostClient {

  private final RestClient restClient;

  public String upload(MultipartFile image, String id) throws ExternalException, ClientErrorException, IOException{

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    HttpHeaders partHeaders = new HttpHeaders();
    partHeaders.setContentType(MediaType.parseMediaType(image.getContentType()));

    HttpEntity<Resource> part = new HttpEntity<>(image.getResource(), partHeaders);

    body.add("file", part);

    ResponseEntity<String> response = restClient
      .post()
      .uri(String.format("http://storage-service:8080/media/%s", id))
      .contentType(MediaType.MULTIPART_FORM_DATA)
      .body(body)
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
