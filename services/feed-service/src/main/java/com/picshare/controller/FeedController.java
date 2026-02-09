package com.picshare.controller;

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

import com.picshare.service.FeedService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feed")
public class FeedController {
  
  private final FeedService feedService;

  @GetMapping("/{id}")
  public ResponseEntity<List<Long>> getFeed(@PathVariable String id){
    List<Long> ret = feedService.getFeed(id);
    return ResponseEntity.ok(ret);
  }

  @PutMapping("/see")
  public ResponseEntity<Void> markAsSeen(@RequestParam String userId, @RequestParam List<Long> postIds){
    postIds.forEach(postId -> feedService.markAsSeen(userId, postId));
    return ResponseEntity.ok().build();
  }
  
  @PostMapping("/add")
  public ResponseEntity<Void> addToFeed(@RequestParam String userId, @RequestParam Long postId){
    feedService.add(userId, postId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
