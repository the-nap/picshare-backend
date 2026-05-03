package com.picshare.post_service.event;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.picshare.post_service.event.events.PostSaveSuccessEvent;
import com.picshare.post_service.event.events.UserDeletedEvent;
import com.picshare.post_service.service.service.PostService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PostEventFunctions {

  private final PostService service;

  @Bean
  public Consumer<PostSaveSuccessEvent> postSaveSuccess() {
    return event -> {
      this.service.confirm(event.postId());
    };
  }
  
  @Bean
  public Consumer<PostSaveSuccessEvent> postSaveFailure() {
    return event -> {
      this.service.deleteByEvent(event.postId());
    };
  }

  @Bean
  public Consumer<UserDeletedEvent> userDeleted() {
    return event -> {
      this.service.deleteByUser(event.userId());
    };
  }
}

