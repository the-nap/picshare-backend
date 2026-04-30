package com.picshare.feed_service.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.picshare.feed_service.service.dto.PostDto;
import com.picshare.feed_service.service.dto.UpdateRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedClient {

  private final RestClient restClient;
  private final DiscoveryClient discoveryClient;

  public List<PostDto> getPosts(List<String> ids){
    ServiceInstance serviceInstance = discoveryClient.getInstances("post-service").get(0);

    UriComponents uriComponents = UriComponentsBuilder
      .fromUriString(String.format("%s/post/feed/posts", serviceInstance.getUri()))
      .build();

    return this.restClient
      .post()
      .uri(uriComponents.expand().toUri())
      .body(ids)
      .retrieve()
      .body(new ParameterizedTypeReference<List<PostDto>>() {});
  }

  public Map<String, String> getPostsForNewConnection(UpdateRequest request){

    ServiceInstance serviceInstance = discoveryClient.getInstances("post-service").get(0);

    UriComponents uriComponents = UriComponentsBuilder
      .fromUriString(String.format("%s/post/feed/connection", serviceInstance.getUri()))
      .build();

    return this.restClient
      .post()
      .uri(uriComponents.expand().toUri())
      .body(request)
      .retrieve()
      .body(new ParameterizedTypeReference<Map<String, String>>() {});
  }

  public List<String> getFollowers(String posterId){
    ServiceInstance serviceInstance = discoveryClient.getInstances("user-service").get(0);
    return this.restClient
      .get()
      .uri(String.format("%s/user/followers/%s", serviceInstance.getUri(), posterId))
      .retrieve()
      .body(new ParameterizedTypeReference<List<String>>() {});
  }
}
