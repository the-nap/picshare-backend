package com.picshare.storage_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.picshare.storage_service.service.exceptions.NoMediaException;
import com.picshare.storage_service.service.exceptions.StorageException;
import com.picshare.storage_service.service.exceptions.UploadException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class StorageControllerAdvice {

  @ExceptionHandler(StorageException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String genericHandler(StorageException se) {
    return se.getMessage();
  }

  @ExceptionHandler(NoMediaException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String defaultHandler(NoMediaException e){
    return e.getMessage();
  }

  @ExceptionHandler(UploadException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String uploadFailHandler(UploadException e){
    return e.getMessage();
  }

}
