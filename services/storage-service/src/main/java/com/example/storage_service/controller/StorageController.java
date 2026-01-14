package com.example.storage_service.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.storage_service.service.StorageService;
import com.example.storage_service.service.exceptions.StorageException;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class StorageController {

  private final StorageService service;
  
  @PostMapping
  public ResponseEntity<?> store(
      @RequestParam("file") MultipartFile file, 
      @RequestParam("id") Long id){

    try(InputStream is = file.getInputStream()){
      service.store(is, id.toString());
    } catch(IOException e) {
      throw new StorageException("Error while reading resource");
    }
    return ResponseEntity
      .ok(
          Map.ofEntries(
            Map.entry("imageId", id))
         );
  }
  
  @GetMapping("/media/{id}")
  public ResponseEntity<Resource> serveMedia(
      @PathVariable Long id) {

      Resource resource = service.serveMedia(id.toString());
      return ResponseEntity.ok(resource);

  }

  @GetMapping("/thumbnail/{id}")
  public ResponseEntity<Resource> serveThumbnail
  (@PathVariable Long id){

    Resource resource = service.serveThumbnail(id.toString());
    return ResponseEntity.ok(resource);

  }
}
