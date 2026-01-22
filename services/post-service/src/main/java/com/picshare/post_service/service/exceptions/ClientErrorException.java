package com.picshare.post_service.service.exceptions;

public class ClientErrorException extends RuntimeException{

  public ClientErrorException(String message) {
    super(message);
  }

  public ClientErrorException(String message, Throwable cause) {
    super(message, cause);
  }
}
