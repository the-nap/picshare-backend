package com.picshare.feed_service.event.events;

import java.time.Instant;

public record ConnectionDeleted(
    String idFollower,
    String idFollowed,
    Instant timestamp
    )
{}
