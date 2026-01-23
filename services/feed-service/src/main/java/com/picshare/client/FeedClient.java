package com.picshare.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.picshare.DTO.UpdateDto;

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

  public List<Long> getFollowers(Long id){
    return this.restClient
      .get()
      .uri("http://user-service/followers/{id}", id)
      .retrieve()
      .body(new ParameterizedTypeReference<List<Long>>() {});
  }
}
