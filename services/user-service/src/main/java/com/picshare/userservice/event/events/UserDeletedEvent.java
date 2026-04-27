package com.picshare.userservice.event.events;

import java.time.Instant;

public record UserDeletedEvent(
    String postId,
    Instant timestamp
    )
{}
