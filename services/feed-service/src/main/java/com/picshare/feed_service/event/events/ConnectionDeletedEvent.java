package com.picshare.feed_service.event.events;

import java.time.Instant;

public record ConnectionDeletedEvent(
    String idFollower,
    String idFollowed,
    Instant timestamp
    )
{}
