package com.picshare.post_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.boot.micrometer.observation.autoconfigure.ObservationProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.picshare.post_service.service.dto.PostRequest;
import com.picshare.post_service.service.dto.PostResponse;
import com.picshare.post_service.service.dto.UpdateRequest;
import com.picshare.post_service.service.service.PostService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {

  private final PostService service;

  // Frontend Endpoint
  @PostMapping(path = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<Void> uploadImage(JwtAuthenticationToken token, @RequestPart(value = "data") MultipartFile data, @Valid @RequestPart(value = "metadata") String metadataJson){
    PostRequest metadata = new ObjectMapper().readValue(metadataJson, PostRequest.class);

      String userId = token.getName();
      this.service.store(data, metadata, userId.split(":")[2]);

      return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable String id, @RequestParam int offset, @RequestParam int max){
    return ResponseEntity
      .ok(this.service.getPostsByUser(id, offset, max));
  }

  @GetMapping("/tags/{tag}")
  public ResponseEntity<List<PostResponse>> getByTags(@PathVariable String tag, @RequestParam int offset, @RequestParam int max){
    return ResponseEntity.ok(service.getPostByTag(tag, offset, max));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostResponse> getPost(@PathVariable String id){
    return ResponseEntity.ok(this.service.getPosts(List.of(id)).getFirst());
  }

  @PostMapping("/{id}/like")
  public ResponseEntity<Integer> addLike(JwtAuthenticationToken token, @PathVariable String id){
    String userId = token.getName().split(":")[2];
    return ResponseEntity.ok(this.service.toggleLike(userId, id));
  }

  @GetMapping("/{id}/likes")
  public ResponseEntity<Boolean> likes(JwtAuthenticationToken token, @PathVariable String id){
    String userId = token.getName().split(":")[2];
    return ResponseEntity.ok(this.service.likes(userId, id));
  }
  
  @DeleteMapping("/{id}/delete")
  public ResponseEntity<Void> delete(JwtAuthenticationToken token, @PathVariable String id){
    String userId = token.getName().split(":")[2];
    this.service.deletePost(id, userId);
    return ResponseEntity.status(HttpStatus.CREATED).build();

  }

  // Feed Service Endpoints
  @PostMapping("/feed/posts")
  public ResponseEntity<List<PostResponse>> serveFeed(@RequestBody List<String> ids){
    return ResponseEntity.ok(service.getPosts(ids));
  }

  @PostMapping("/feed/connection")
  public ResponseEntity<Map<String,String>> getUpdates(@RequestBody UpdateRequest request){
    return ResponseEntity.ok(service.getPosts(request));
  }
}
