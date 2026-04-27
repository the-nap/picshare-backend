package com.picshare.feed_service.event.events;

import java.time.Instant;

public record UserDeletedEvent(
    String postId,
    Instant timestamp
    )
{}
