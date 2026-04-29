package com.picshare.feed_service.service.exceptions;

public class FeedNotFoundException extends RuntimeException{

  public FeedNotFoundException(String message){
    super(message);
  }
  
  public FeedNotFoundException(String message, Throwable cause){
    super(message, cause);
  }
}
