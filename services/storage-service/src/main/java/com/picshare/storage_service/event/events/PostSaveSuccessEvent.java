package com.picshare.storage_service.event.events;

import java.time.Instant;

public record PostSaveSuccessEvent(
    String postId,
    Instant timestamp
    )
{}
