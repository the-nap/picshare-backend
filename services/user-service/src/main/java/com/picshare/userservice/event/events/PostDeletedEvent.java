package com.picshare.userservice.event.events;

import java.time.Instant;

public record PostDeletedEvent(
    String postId,
    Instant timestamp
    ) 
{}
