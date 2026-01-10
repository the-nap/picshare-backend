package com.example.storage_service.service.exceptions;

public class WebpConversionException extends RuntimeException{

  public WebpConversionException(String message) {
    super(message);
  }
  
  public WebpConversionException(String message, Throwable cause) {
    super(message, cause);
  }
}
