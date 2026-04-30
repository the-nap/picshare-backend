package com.picshare.post_service.event;

import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.picshare.post_service.event.events.PostConfirmedEvent;
import com.picshare.post_service.event.events.PostSavedSuccessEvent;
import com.picshare.post_service.event.events.UserDeletedEvent;
import com.picshare.post_service.service.service.PostService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PostEventFunctions {

  private final PostService service;

  @Bean
  public Function<PostSavedSuccessEvent, PostConfirmedEvent> postSavedSuccess() {
    return event -> new PostConfirmedEvent(
        this.service.confirm(event.postId()),
        event.postId(),
        Instant.now()
        );
  }

  @Bean
  public Consumer<UserDeletedEvent> userDeleted() {
    return event -> {
      this.service.deleteByUser(event.userId());
    };
  }
}

