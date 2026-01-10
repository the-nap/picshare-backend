package com.example.storage_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.storage_service.service.exceptions.StorageException;

@RestControllerAdvice
public class StorageControllerAdvice {

  @ExceptionHandler(StorageException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String GenericHandler(StorageException se) {
    return se.getMessage();
  }

  
}
