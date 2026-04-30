package com.picshare.userservice.event;

import java.time.Instant;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import com.picshare.userservice.event.events.ConnectionCreated;
import com.picshare.userservice.event.events.ConnectionDeleted;
import com.picshare.userservice.event.events.UserDeletedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserEventProducer {

  private final StreamBridge streamBridge;

  public boolean sendUserDeletedEvent(String userId){
    return this.streamBridge.send("userDeleted-out-0",
        new UserDeletedEvent(userId, Instant.now()));
  }

  public boolean sendConnectionCreatedEvent(String followerId, String followedId){
    return this.streamBridge.send("connectionCreated-out-0",
        new ConnectionCreated(followerId, followedId, Instant.now()));
  }
  
  public boolean sendConnectionDeletedEvent(String followerId, String followedId){
    return this.streamBridge.send("connectionDeleted-out-0",
        new ConnectionDeleted(followerId, followedId, Instant.now()));
  }
  
}
