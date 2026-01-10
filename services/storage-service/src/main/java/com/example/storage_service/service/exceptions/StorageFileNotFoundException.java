package com.example.storage_service.service.exceptions;

public class StorageFileNotFoundException extends StorageException{

  public StorageFileNotFoundException(String message) {
    super(message);
  }

  public StorageFileNotFoundException(String message, Throwable cause) {
    super(message,cause);
  }

}
