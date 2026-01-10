package com.example.storage_service.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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
  public Long store(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id){
    try(InputStream is = file.getInputStream()){
      service.store(is, id.toString());
    } catch(IOException e) {
      throw new StorageException("Error while reading resource");
    }
    return id;
  }
  
  @GetMapping("/image/{id}")
  public Resource serveImage(@PathVariable Long id) {
    return service.load(id.toString());
  }

  @GetMapping("/image/")
  public List<Resource> serveAll(@RequestParam List<Long> ids){
    List<String> stringIds = ids.stream()
      .map(Object::toString)
      .collect(Collectors.toList());
    return service.loadThumbnail(stringIds)
      .collect(Collectors.toList());
  }
}
