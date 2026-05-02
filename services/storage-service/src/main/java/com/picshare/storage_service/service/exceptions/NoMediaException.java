package com.picshare.storage_service.service.exceptions;

public class NoMediaException extends RuntimeException{

  public NoMediaException(String message){
    super(message);
  }

  public NoMediaException(String message, Throwable throwable){
    super(message, throwable);
  }
}
