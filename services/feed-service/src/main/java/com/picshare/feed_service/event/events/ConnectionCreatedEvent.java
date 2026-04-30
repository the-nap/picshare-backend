package com.picshare.feed_service.event.events;

import java.time.Instant;

public record ConnectionCreatedEvent(
    String idFollower,
    String idFollowed,
    Instant timestamp
    )
{}
