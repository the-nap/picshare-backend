package com.picshare.userservice.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.picshare.userservice.dto.UserDTO;
import com.picshare.userservice.service.UserService;

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

  @PutMapping("/avatar/{id}")
  public ResponseEntity<Void> uploadAvatar(@PathVariable String id, @RequestBody MultipartFile image){
    try(InputStream stream = image.getInputStream()){

      this.userService.uploadAvatar(id, stream);
      return ResponseEntity.ok().build();

    }catch(IOException e){
      return ResponseEntity.badRequest().build();
    }
    
  }
  @GetMapping("/name/{username}")
  public ResponseEntity<UserDTO> getByUsername(@PathVariable String username){
    return ResponseEntity.ok(this.userService.getByUsername(username));
  }

  @GetMapping("/contains/{toSearch}")
  public ResponseEntity<List<UserDTO>> searchUsers(@PathVariable String toSearch, @RequestParam int offset, @RequestParam int max){
    return ResponseEntity.ok(this.userService.search(toSearch, offset, max));

  }

  @PostMapping("/follow")
  public ResponseEntity<Void> addFollower(@AuthenticationPrincipal OAuth2User principal, @RequestBody String toFollow){

    String userId = (String) principal.getAttribute("sub");
    this.userService.follow(userId.split(":")[2], toFollow);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/unfollow")
  public ResponseEntity<Void> removeFollower(@AuthenticationPrincipal OAuth2User principal, @RequestBody String toFollow){

    String userId = (String) principal.getAttribute("sub");
    this.userService.unfollow(userId.split(":")[2], toFollow);

    return ResponseEntity.ok().build();
  }

}


