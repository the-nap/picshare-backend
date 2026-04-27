package com.picshare.userservice.event.events;

import java.time.Instant;

public record ConnectionCreated(
    String idFollower,
    String idFollowed,
    Instant timestamp)
{}
