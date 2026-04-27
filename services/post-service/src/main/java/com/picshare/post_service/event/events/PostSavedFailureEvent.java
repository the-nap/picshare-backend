package com.picshare.post_service.event.events;

import java.time.Instant;

public record PostSavedFailureEvent(
    String postId,
    Instant timestamp
    )
{}
