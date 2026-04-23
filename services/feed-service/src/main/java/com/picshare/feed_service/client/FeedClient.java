package com.picshare.feed_service.client;

import java.util.List;

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

  public List<UpdateDto> getUpdates(){
    return this.restClient
      .get()
      .uri("http://post-service/updates")
      .retrieve()
      .body(new ParameterizedTypeReference<List<UpdateDto>>(){});
  }

  public List<PostDto> getPosts(List<String> ids){

    UriComponents uriComponents = UriComponentsBuilder
      .fromUriString("http://post-service:8080/post/feed")
      .queryParam("id", ids.toArray())
      .build();

    return this.restClient
      .get()
      .uri(uriComponents.expand().toUri())
      .retrieve()
      .body(new ParameterizedTypeReference<List<PostDto>>() {});
  }

  public List<String> getFollowers(String id){
    return this.restClient
      .get()
      .uri("http://user-service:8080/followers/{id}", id)
      .retrieve()
      .body(new ParameterizedTypeReference<List<String>>() {});
  }
}
