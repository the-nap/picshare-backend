package com.picshare.post_service.event.events;

import java.time.Instant;

public record PostDeletedEvent(
    String postId,
    Instant timestamp
    ) 
{}
