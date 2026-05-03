package com.picshare.post_service.service.exceptions;

public class OperationNotAllowedException extends RuntimeException{

  public OperationNotAllowedException(String message){
    super(message);
  }

  public OperationNotAllowedException(String message, Throwable cause){
    super(message, cause);
  }
}
