package com.picshare.post_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.picshare.post_service.service.exceptions.ExternalServiceException;
import com.picshare.post_service.service.exceptions.OperationNotAllowedException;
import com.picshare.post_service.service.exceptions.PostNotFoundException;

@RestControllerAdvice
public class PostControllerAdvice {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)  
  public Map<String, String> handleValidationException(MethodArgumentNotValidException ex){
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      errors.put(((FieldError) error).getField(), error.getDefaultMessage());
    });
    return errors;
  }

  @ExceptionHandler(PostNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String notFoundHandler(PostNotFoundException pe){
    return pe.getMessage();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(ExternalServiceException.class)
  public String externalError(ExternalServiceException e){
    return e.getMessage();
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(OperationNotAllowedException.class)
  public void notAllowedHandler(OperationNotAllowedException e){}
}
