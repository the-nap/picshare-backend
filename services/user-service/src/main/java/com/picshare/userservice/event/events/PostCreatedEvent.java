package com.picshare.userservice.event.events;

import java.time.Instant;

public record PostCreatedEvent(
    String postId,
    Instant timestamp
    )
{}
