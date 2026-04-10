package com.example.storage_service.service.exceptions;

public class NoAvatarException extends RuntimeException{

  public NoAvatarException(String message){
    super(message);
  }

  public NoAvatarException(String message, Throwable throwable){
    super(message, throwable);
  }
}
