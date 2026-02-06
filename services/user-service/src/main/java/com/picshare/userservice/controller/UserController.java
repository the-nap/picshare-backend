package com.picshare.userservice.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picshare.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
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
  
  @PostMapping("/follow")
  public ResponseEntity<Void> addFollower(@RequestBody String userId, @RequestBody String toFollowId){

    return null;
  }

  @PostMapping("/create")
  public ResponseEntity<Void> addUser(@RequestBody Map<String, String> body){
    //this.userService.createUser(body.get("userId"), body.get("email"), body.get("username"));
    return ResponseEntity.ok().build();
  }

  @GetMapping("/this")
  public ResponseEntity<String> getCurrent(Principal principal){
    String username = principal.getName();
}


