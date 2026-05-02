package com.picshare.post_service.event.events;

import java.time.Instant;

public record PostSaveFailureEvent(
    String postId,
    Instant timestamp
    )
{}
