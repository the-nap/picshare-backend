package com.picshare.userservice.event.events;

import java.time.Instant;

public record ConnectionDeleted(
    String idFollower,
    String idFollowed,
    Instant timestamp
    )
{}
