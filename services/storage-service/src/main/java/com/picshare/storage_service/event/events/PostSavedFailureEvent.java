package com.picshare.storage_service.event.events;

import java.time.Instant;

public record PostSavedFailureEvent(
    String postId,
    Instant timestamp
    )
{}
