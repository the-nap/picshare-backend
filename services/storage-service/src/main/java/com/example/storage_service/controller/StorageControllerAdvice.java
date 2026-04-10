package com.example.storage_service.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.storage_service.service.exceptions.NoAvatarException;
import com.example.storage_service.service.exceptions.StorageException;

@RestControllerAdvice
public class StorageControllerAdvice {

  @ExceptionHandler(StorageException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String genericHandler(StorageException se) {
    return se.getMessage();
  }

  @ExceptionHandler(NoAvatarException.class)
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<Void> defaultHandler(NoAvatarException e){
    return ResponseEntity
      .status(HttpStatus.FOUND)
      .location(URI.create("/avatar"))
      .build();
  }
  
}
