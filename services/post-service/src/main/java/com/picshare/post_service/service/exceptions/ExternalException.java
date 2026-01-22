package com.picshare.post_service.service.exceptions;

public class ExternalException extends RuntimeException{

  public ExternalException(String message) {
    super(message);
  }

  public ExternalException(String message, Throwable cause) {
    super(message, cause);
  }
}
