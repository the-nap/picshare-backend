package com.example.storage_service.controller;


import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.storage_service.service.StorageService;
import com.example.storage_service.service.exceptions.StorageException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/media")
public class StorageController {

  private final StorageService service;
  
  @PostMapping("/{id}")
  public ResponseEntity<Void> store(@RequestParam("file") MultipartFile image, @PathVariable String id){

    service.store(image, id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/avatar/{id}")
  public ResponseEntity<Void> storeAvatar(@RequestParam("file") MultipartFile image, @PathVariable String id){
    try {
      service.storeAvatar(image, id);
    }catch (Exception e) {
      throw new StorageException("Error while reading resource");
    }
    return ResponseEntity.ok().build();
  }

  @GetMapping("/avatar/{id}")
  public ResponseEntity<Resource> serveAvatar(@PathVariable String id){
    return ResponseEntity.ok(service.serveAvatar(id));
  }

  @GetMapping("/avatar")
  public ResponseEntity<Resource> serveDefault(){
    return ResponseEntity.ok(service.serveAvatar("default"));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Resource> serveMedia(@PathVariable String id) {
      return ResponseEntity.ok()
        .body(
          service.serveMedia(id));
  }

  @GetMapping("/preview/{id}")
  public ResponseEntity<Resource> servePreview(@PathVariable String id){
    return ResponseEntity.ok()
      .body(
          service.servePreview(id));
  }
}
