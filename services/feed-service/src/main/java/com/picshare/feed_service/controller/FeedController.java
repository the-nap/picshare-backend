package com.picshare.feed_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.picshare.feed_service.service.dto.PostDto;
import com.picshare.feed_service.service.service.FeedService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feed")
public class FeedController {
  
  private final FeedService feedService;

  @GetMapping
  public ResponseEntity<List<PostDto>> getFeed(JwtAuthenticationToken token, @RequestParam int offset, @RequestParam int max){
    String userId = token.getName().split(":")[2];

    return ResponseEntity.ok(feedService.getFeed(userId, offset, max));
  }
}
