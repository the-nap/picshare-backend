package com.example.storage_service.service;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;

public interface StorageService {

  void store(InputStream file, String id);

  Resource serveMedia(String id);

  Resource serveThumbnail(String id);

  void deleteAll();
  
}


