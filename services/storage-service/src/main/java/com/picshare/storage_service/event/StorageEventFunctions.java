package com.picshare.storage_service.event;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.picshare.storage_service.event.events.PostDeletedEvent;
import com.picshare.storage_service.event.events.UserDeletedEvent;
import com.picshare.storage_service.service.StorageService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class StorageEventFunctions {

  private final StorageService service;

  @Bean
  public Consumer<PostDeletedEvent> deletePost(){
    return event -> this.service.deleteMedia(event.postId());
  }

  @Bean
  public Consumer<UserDeletedEvent> deleteUser(){
    return event -> this.service.deleteAvatar(event.userId());
  }

  
}
