package com.example.storage_service.service;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;

public interface StorageService {

  void store(InputStream file, String filename);

  Resource load(String filename);

  Stream<Resource> loadThumbnail(List<String> filenames);

  void deleteAll();
  
}


