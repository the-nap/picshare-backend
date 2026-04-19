package com.example.storage_service.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  void store(MultipartFile file, String id);

  void storeAvatar(MultipartFile file, String id);

  Resource serveAvatar(String id);

  Resource serveMedia(String id);

  Resource servePreview(String id);

}


