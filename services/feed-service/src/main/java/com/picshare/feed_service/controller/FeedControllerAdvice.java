package com.picshare.feed_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.picshare.feed_service.service.exceptions.ExternalServiceException;
import com.picshare.feed_service.service.exceptions.FeedNotFoundException;

@RestControllerAdvice
public class FeedControllerAdvice {
  
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(FeedNotFoundException.class)
  public String notFound(FeedNotFoundException e){
    return e.getMessage();
  }

  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  @ExceptionHandler(ExternalServiceException.class)
  public String serviceNotFound(ExternalServiceException e){
    return e.getMessage();
  }

  
}
