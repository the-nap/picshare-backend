package com.picshare.storage_service.event.events;

import java.time.Instant;

public record PostDeletedEvent(
    String postId,
    Instant timestamp
    ) 
{}
