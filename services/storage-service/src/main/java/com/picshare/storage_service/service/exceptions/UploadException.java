package com.picshare.storage_service.service.exceptions;

public class UploadException extends RuntimeException{

  public UploadException(String message){
    super(message);
  }

  public UploadException(String message, Throwable cause){
    super(message, cause);
  }
  
}
