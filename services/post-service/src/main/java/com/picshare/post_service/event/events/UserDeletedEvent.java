package com.picshare.post_service.event.events;

import java.time.Instant;

public record UserDeletedEvent(
    String userId,
    Instant timestamp
    )
{}
