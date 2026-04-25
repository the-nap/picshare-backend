package com.picshare.userservice.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.picshare.userservice.service.dto.UserDTO;
import com.picshare.userservice.service.service.UserService;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getUser(@PathVariable String id){
    return ResponseEntity.ok(this.userService.getUser(id));
  }

  @PutMapping("/upload")
  public ResponseEntity<Void> uploadAvatar(JwtAuthenticationToken token, @Nullable @RequestParam MultipartFile media, @Nullable @RequestParam String data){

    String userId = token.getName().split(":")[2];
    if(media != null)
        this.userService.uploadAvatar(userId, media);

    if(data != null)
      this.userService.updateBio(userId, data);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/name/{username}")
  public ResponseEntity<UserDTO> getByUsername(@PathVariable String username){
    return ResponseEntity.ok(this.userService.getByUsername(username));
  }

  @GetMapping("/contains")
  public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String toSearch, @RequestParam int offset, @RequestParam int max){
    return ResponseEntity.ok(this.userService.search(toSearch, offset, max));
  }

  @GetMapping("/follows")
  public ResponseEntity<Boolean> follows(JwtAuthenticationToken token, @RequestParam String followed){

    String userId = token.getName();
    return ResponseEntity.ok(this.userService.follows(userId.split(":")[2], followed));
  }


  @PostMapping(value = "/follow", produces = "text/plain")
  public ResponseEntity<String> addFollower(JwtAuthenticationToken token, @RequestBody Map<String, String> body){

    String userId = token.getName();
    String username = this.userService.follow(userId.split(":")[2], body.get("toFollow"));

    return ResponseEntity.ok().body(username);
  }

  @PostMapping(value = "/unfollow", produces = "text/plain")
  public ResponseEntity<String> removeFollower(JwtAuthenticationToken token, @RequestBody Map<String, String> body){

    String userId = token.getName();
    String username = this.userService.unfollow(userId.split(":")[2], body.get("toUnfollow"));

    return ResponseEntity.ok().body(username);
  }
}
