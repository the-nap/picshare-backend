package com.picshare.userservice.service.exceptions;

public class UserNotFoundException extends RuntimeException{

  public UserNotFoundException(String type, String message){
    super(String.format("User not found with %s: %s", type, message));
  }

  public UserNotFoundException(String message, Throwable cause){
    super(message, cause);
  }

}
