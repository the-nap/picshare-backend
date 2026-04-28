package com.picshare.post_service.event;

import java.time.Instant;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.picshare.post_service.event.events.PostConfirmedEvent;
import com.picshare.post_service.event.events.PostCreatedEvent;
import com.picshare.post_service.event.events.PostDeletedEvent;
import com.picshare.post_service.event.events.PostSavedSuccessEvent;
import com.picshare.post_service.service.dto.UpdateDto;
import com.picshare.post_service.service.service.PostService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PostEventFunctions {

  private final PostService service;

  @Bean
  public Function<UpdateDto, PostCreatedEvent> postCreated() {
    return update -> new PostCreatedEvent(update.getPostId(), update.getUserId(), Instant.now());
  }

  @Bean
  public Function<String, PostDeletedEvent> postDeleted() {
    return id -> new PostDeletedEvent(id, Instant.now());
  }

  @Bean
  public Function<PostSavedSuccessEvent, PostConfirmedEvent> postSaved() {
    return event -> {
      UpdateDto update = this.service.confirm(event.postId());
      return new PostConfirmedEvent(update.getUserId(), update.getPostId(), Instant.now());
    };
  }


  
}

