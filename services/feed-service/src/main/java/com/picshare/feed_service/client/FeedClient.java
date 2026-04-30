package com.picshare.feed_service.client;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.picshare.feed_service.service.dto.UpdateRequest;
import com.picshare.feed_service.service.dto.UpdateResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedClient {

  private final RestClient restClient;
  private final DiscoveryClient discoveryClient;

  public UpdateResponse getPosts(UpdateRequest request){

    ServiceInstance serviceInstance = discoveryClient.getInstances("post-service").get(0);

    UriComponents uriComponents = UriComponentsBuilder
      .fromUriString(String.format("%s/post/feed", serviceInstance.getUri()))
      .build();

    return this.restClient
      .post()
      .uri(uriComponents.expand().toUri())
      .body(request)
      .retrieve()
      .body(new ParameterizedTypeReference<UpdateResponse>() {});
  }

  public List<String> getFollowers(String id){
    ServiceInstance serviceInstance = discoveryClient.getInstances("user-service").get(0);
    return this.restClient
      .get()
      .uri(String.format("%s/followers/%s", serviceInstance.getUri(), id))
      .retrieve()
      .body(new ParameterizedTypeReference<List<String>>() {});
  }
}
