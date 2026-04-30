package com.picshare.post_service.event;

import java.time.Instant;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import com.picshare.post_service.event.events.PostDeletedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostEventProducer {

  private final StreamBridge streamBridge;
  
  public boolean sendPostDeletedEvent(String postId) {

    return this.streamBridge.send("postDeleted-out-0",
        new PostDeletedEvent(postId, Instant.now()));

  }
}
