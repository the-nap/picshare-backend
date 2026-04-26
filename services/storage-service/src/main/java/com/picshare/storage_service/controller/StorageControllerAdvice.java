package com.picshare.storage_service.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.picshare.storage_service.service.StorageService;
import com.picshare.storage_service.service.exceptions.NoAvatarException;
import com.picshare.storage_service.service.exceptions.StorageException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class StorageControllerAdvice {

  private final StorageService service;

  @ExceptionHandler(StorageException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String genericHandler(StorageException se) {
    return se.getMessage();
  }

  @ExceptionHandler(NoAvatarException.class)
  ResponseEntity<Resource> defaultHandler(NoAvatarException e){
    return ResponseEntity
      .ok(service.serveAvatar("default"));
  }
  
}
