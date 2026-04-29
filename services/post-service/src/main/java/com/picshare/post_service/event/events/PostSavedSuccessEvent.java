package com.picshare.post_service.event.events;

import java.time.Instant;

public record PostSavedSuccessEvent(
    String postId,
    Instant timestamp
    )
{}
