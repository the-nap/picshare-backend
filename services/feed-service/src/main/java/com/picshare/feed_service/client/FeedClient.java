package com.picshare.feed_service.client;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.picshare.feed_service.service.dto.PostDto;
import com.picshare.feed_service.service.dto.UpdateDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedClient {

  private final RestClient restClient;
  private final DiscoveryClient discoveryClient;

  public List<UpdateDto> getUpdates(){
    ServiceInstance serviceInstance = discoveryClient.getInstances("post-service").get(0);
    return this.restClient
      .get()
      .uri(String.format("%s/updates", serviceInstance.getUri()))
      .retrieve()
      .body(new ParameterizedTypeReference<List<UpdateDto>>(){});
  }

  public List<PostDto> getPosts(List<String> ids){

    ServiceInstance serviceInstance = discoveryClient.getInstances("post-service").get(0);

    UriComponents uriComponents = UriComponentsBuilder
      .fromUriString(String.format("%s/post/feed", serviceInstance.getUri()))
      .queryParam("id", ids.toArray())
      .build();

    return this.restClient
      .get()
      .uri(uriComponents.expand().toUri())
      .retrieve()
      .body(new ParameterizedTypeReference<List<PostDto>>() {});
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
