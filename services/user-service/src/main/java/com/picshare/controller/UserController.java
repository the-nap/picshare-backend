package com.picshare.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.picshare.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("followers/{id}")
  public ResponseEntity<List<String>> getFollowers(@PathVariable String id){
    List<String> followers = userService.getFollowers(id)
      .stream()
      .map(user -> user.getId())
      .collect(Collectors.toList());

    return ResponseEntity
      .ok()
      .body(followers);
  }
  
  @PostMapping("/user/follow")
  public ResponseEntity<Void> addFollower(@RequestParam String userId, @RequestParam String toFollowId){

    return null;
  }
}


