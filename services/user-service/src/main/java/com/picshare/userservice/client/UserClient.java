package com.picshare.userservice.client;

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
import com.picshare.userservice.service.exceptions.*;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserClient {

  private final RestClient restClient;
  private final DiscoveryClient discoveryClient;

  public void uploadAvatar(MultipartFile image, String id){

    ServiceInstance serviceInstance;
    try{
      serviceInstance = discoveryClient.getInstances("storage-service").get(0);
    } catch(IndexOutOfBoundsException ioe){
      throw new ExternalServiceException( ioe.getMessage() );
    }

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    HttpHeaders partHeaders = new HttpHeaders();
    partHeaders.setContentType(MediaType.parseMediaType(image.getContentType()));

    HttpEntity<Resource> part = new HttpEntity<>(image.getResource(), partHeaders);

    body.add("file", part);


     this.restClient
      .post()
      .uri(String.format("%s/media/avatar/%s", serviceInstance.getUri(), id))
      .body(body)
      .contentType(MediaType.MULTIPART_FORM_DATA)
      .retrieve()
      .toBodilessEntity();
  }

}

