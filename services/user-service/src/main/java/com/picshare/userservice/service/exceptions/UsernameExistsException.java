package com.picshare.userservice.service.exceptions;


public class UsernameExistsException extends RuntimeException{

  public UsernameExistsException(String message){
    super(message);
  }

  public UsernameExistsException(String message, Throwable cause){
    super(message, cause);
  }

}
