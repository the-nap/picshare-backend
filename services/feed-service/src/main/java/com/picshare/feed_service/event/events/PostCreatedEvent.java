package com.picshare.feed_service.event.events;

import java.time.Instant;

public record PostCreatedEvent(
    String postId,
    Instant timestamp
    )
{}
