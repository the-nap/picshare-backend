package com.picshare.post_service.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.picshare.post_service.dto.PostRequest;
import com.picshare.post_service.dto.PostResponse;
import com.picshare.post_service.dto.UpdateDto;
import com.picshare.post_service.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class PostController {

  private final PostService service;

  @PostMapping("/image/upload")
  public ResponseEntity<Void> uploadImage(
      @RequestPart("image") MultipartFile image,
      @RequestPart("metadata") PostRequest metadata){
      
      try(InputStream stream = image.getInputStream()){
        this.service.store(stream, metadata);
      }catch(IOException e){}

      return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/post/{id}")
  public ResponseEntity<PostResponse> servePost(@PathVariable Long id){
    return ResponseEntity
      .ok()
      .body(this.service.serve(id));
  }

  @GetMapping("update/")
  public ResponseEntity<List<UpdateDto>> getUpdates(){
    return ResponseEntity
      .ok()
      .body(this.service.serveUpdates());
  }
}
