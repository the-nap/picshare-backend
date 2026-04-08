package com.example.storage_service.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.storage_service.service.StorageService;
import com.example.storage_service.service.exceptions.StorageException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StorageController {

  private final StorageService service;
  
  @PostMapping("/store/{id}")
  public ResponseEntity<Void> store(@RequestParam("file") MultipartFile file, @PathVariable String id){

    try(InputStream is = file.getInputStream()){
      service.store(is, id);
    } catch(IOException e) {
      throw new StorageException("Error while reading resource");
    }
    return ResponseEntity.ok().build();
  }

  @PostMapping("/avatar/{id}")
  public ResponseEntity<Void> storeAvatar(@RequestPart(value = "image") MultipartFile image, @PathVariable String id){
    try(InputStream is = image.getInputStream()){
      service.storeAvatar(is, id);
    }catch (IOException e) {
      throw new StorageException("Error while reading resource");
    }
    return ResponseEntity.ok().build();
  }

  
  @GetMapping("/media/{id}")
  public ResponseEntity<Resource> serveMedia(@PathVariable String id) {
      return ResponseEntity.ok()
        .body(
          service.serveMedia(id));
  }

  @GetMapping("/thumbnail/{id}")
  public ResponseEntity<Resource> serveThumbnail(@PathVariable String id){
    return ResponseEntity.ok()
      .body(
          service.serveThumbnail(id));
  }
}
