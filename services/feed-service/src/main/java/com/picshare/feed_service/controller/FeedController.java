package com.picshare.feed_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.picshare.feed_service.DTO.PostDto;
import com.picshare.feed_service.service.FeedService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feed")
public class FeedController {
  
  private final FeedService feedService;

  @GetMapping
  public ResponseEntity<List<PostDto>> getFeed(@AuthenticationPrincipal OAuth2User user, @RequestParam int offset, @RequestParam int max){
    String userId = (String) user.getAttribute("sub");

    return ResponseEntity.ok(feedService.getFeed(userId.split(":")[2], offset, max));
  }

  @PutMapping("/see")
  public ResponseEntity<Void> markAsSeen(@RequestParam String userId, @RequestParam List<String> postIds){
    postIds.forEach(postId -> feedService.markAsSeen(userId, postId));
    return ResponseEntity.ok().build();
  }
  
  @PostMapping("/add")
  public ResponseEntity<Void> addToFeed(@RequestParam String userId, @RequestParam String postId){
    feedService.add(userId, postId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
