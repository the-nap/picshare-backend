package com.picshare.post_service.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.picshare.post_service.dto.PostResponse;
import com.picshare.post_service.dto.UpdateDto;
import com.picshare.post_service.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {

  private final PostService service;

  @GetMapping("/feed")
  public ResponseEntity<List<PostResponse>> serveFeed(@RequestParam List<String> ids){
    return ResponseEntity.ok(service.getPosts(ids));
  }

  @PostMapping("/image/upload")
  public ResponseEntity<Void> uploadImage(
      @AuthenticationPrincipal Jwt jwt,
      @RequestParam("media") MultipartFile image,
      @RequestParam("data") String data){

      String userId = jwt.getPrincipalClaimName();
      System.out.println(userId);

      System.out.println(image.getSize());
      System.out.println(data);

      try(InputStream stream = image.getInputStream()){
        this.service.store(stream, data, userId.split(":")[2]);
      }catch(IOException e){}

      return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostResponse> servePost(@PathVariable String id){
    return ResponseEntity
      .ok()
      .body(this.service.serve(id));
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable String id, @RequestParam int offset, @RequestParam int max){
    return ResponseEntity
      .ok(this.service.getPostsByUser(id, offset, max));
  }

  @GetMapping("/update")
  public ResponseEntity<List<UpdateDto>> getUpdates(){
    return ResponseEntity
      .ok()
      .body(this.service.serveUpdates());
  }

  @GetMapping("/tags/{tag}")
  public ResponseEntity<List<PostResponse>> getByTags(@PathVariable String tag, @RequestParam int offset, @RequestParam int max){
    return ResponseEntity.ok(service.getPostByTag(tag, offset, max));
  }
}
