package com.picshare.storage_service.event;

import java.time.Instant;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import com.picshare.storage_service.event.events.PostSaveFailureEvent;
import com.picshare.storage_service.event.events.PostSaveSuccessEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StorageEventProducer {

  private final StreamBridge streamBridge;

  public boolean sendPostSaveSuccessEvent(String postId) {

    return streamBridge.send("postSaveSuccess-out-0",
        new PostSaveSuccessEvent(postId, Instant.now()));
  }
  
  public boolean sendPostSaveFailureEvent(String postId) {

    return streamBridge.send("postSaveFailure-out-0",
        new PostSaveFailureEvent(postId, Instant.now()));
  }
}
