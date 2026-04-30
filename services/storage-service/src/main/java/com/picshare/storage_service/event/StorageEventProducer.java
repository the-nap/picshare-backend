package com.picshare.storage_service.event;

import java.time.Instant;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import com.picshare.storage_service.event.events.PostSavedSuccessEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StorageEventProducer {

  private final StreamBridge streamBridge;

  public boolean sendPostSavedEvent(String postId) {

    return streamBridge.send("postSavedSuccess-out-0",
        new PostSavedSuccessEvent(postId, Instant.now()));

  }
  
}
