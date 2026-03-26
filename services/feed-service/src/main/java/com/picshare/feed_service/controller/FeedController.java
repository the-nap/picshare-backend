package com.picshare.feed_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @GetMapping("/{id}")
  public ResponseEntity<List<PostDto>> getFeed(@PathVariable String id, @RequestParam int offset, @RequestParam int max){
    return ResponseEntity.ok(feedService.getFeed(id, offset, max));
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
