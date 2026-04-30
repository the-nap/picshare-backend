package com.picshare.post_service.client;

import java.io.IOException;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import com.picshare.post_service.service.exceptions.ExternalException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostClient {

  private final RestClient restClient;
  private final DiscoveryClient discoveryClient;

  public void upload(MultipartFile image, String id) throws ExternalException{

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    HttpHeaders partHeaders = new HttpHeaders();
    partHeaders.setContentType(MediaType.parseMediaType(image.getContentType()));

    HttpEntity<Resource> part = new HttpEntity<>(image.getResource(), partHeaders);

    body.add("file", part);

    ServiceInstance serviceInstance = discoveryClient.getInstances("storage-service").get(0);

    try{ 
      this.restClient
        .post()
        .uri(String.format("%s/media/%s", serviceInstance.getUri(), id))
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .body(body)
        .retrieve()
        .toBodilessEntity();
    } catch (Exception e){
      throw new ExternalException(e.getMessage());
    }

  }
}
