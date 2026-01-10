package com.picshare.post_service.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.picshare.post_service.dto.ImageMetadata;
import com.picshare.post_service.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class PostController {

  private final PostService service;

  @PostMapping
  public void uploadImage(
      @RequestPart("data") MultipartFile data,
      @RequestPart("metadata") ImageMetadata metadata) {
      try (InputStream bar = data.getInputStream()){
        service.store(bar, metadata);

        } catch(IOException e){
          // TODO handle exception
        }
  }

  
}
