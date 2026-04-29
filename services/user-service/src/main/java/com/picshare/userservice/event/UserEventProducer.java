package com.picshare.userservice.event;

import java.time.Instant;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import com.picshare.userservice.event.events.UserDeletedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserEventProducer {

  private final StreamBridge streamBridge;

  public boolean sendUserDeletedEvent(String userId){
    return this.streamBridge.send("userDeletedEvent-out-0",
        new UserDeletedEvent(userId, Instant.now()));
  }
  
}
