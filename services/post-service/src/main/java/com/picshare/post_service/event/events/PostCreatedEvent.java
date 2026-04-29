package com.picshare.post_service.event.events;

import java.time.Instant;

public record PostCreatedEvent(
    String postId,
    String userId,
    Instant timestamp
    )
{}
