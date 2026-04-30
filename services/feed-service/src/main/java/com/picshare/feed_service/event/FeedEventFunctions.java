package com.picshare.feed_service.event;

import java.util.function.Consumer;

import org.springframework.context.annotation.Configuration;

import com.picshare.feed_service.event.events.ConnectionCreatedEvent;
import com.picshare.feed_service.event.events.ConnectionDeletedEvent;
import com.picshare.feed_service.event.events.PostConfirmedEvent;
import com.picshare.feed_service.event.events.PostDeletedEvent;
import com.picshare.feed_service.event.events.UserDeletedEvent;
import com.picshare.feed_service.service.service.FeedService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FeedEventFunctions {

  private final FeedService service;

  public Consumer<UserDeletedEvent> userDeleted(){
    return event -> service.userDeleted(event.userId());
  }

  public Consumer<PostDeletedEvent> postDeleted(){
    return event -> service.postDeleted(event.postId());
  }

  public Consumer<ConnectionCreatedEvent> connectionCreated(){
    return event -> service.connectionCreated(event.idFollower(), event.idFollowed());
  }

  public Consumer<ConnectionDeletedEvent> connectionDeleted(){
    return event -> service.connectionDeleted(event.idFollower(), event.idFollowed());
  }

  public Consumer<PostConfirmedEvent> postConfirmed(){
    return event -> service.postConfirmed(event.postId(), event.userId());
  }
}
