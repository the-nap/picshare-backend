package com.picshare.post_service.event.events;

import java.time.Instant;

public record PostConfirmedEvent(
    String userId,
    String postId,
    Instant timestamp
    )
{}
