package com.picshare.userservice.service;

import org.springframework.stereotype.Service;

import com.picshare.userservice.dto.UserDTO;
import com.picshare.userservice.mapper.UserMapper;
import com.picshare.userservice.repository.UserRepository;
import com.picshare.userservice.service.exceptions.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAuthService {

  private final UserRepository repository;
  private final UserMapper mapper;


  public UserDTO getById(String id){
    return repository.findById(id)
      .map(mapper::toDto)
      .orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %s", id)));
  }

  public UserDTO getByUsername(String username){
    return repository.findByUsername(username)
      .map(mapper::toDto)
      .orElseThrow(() -> new UserNotFoundException(String.format("User not found with username: %s", username)));
  }

  public UserDTO getByEmail(String email){
    return repository.findByEmail(email)
      .map(mapper::toDto)
      .orElseThrow(() -> new UserNotFoundException(String.format("User not found with email: %s", email)));
  }

  public boolean checkPassword(String id, String password){
    return repository.findById(id)
      .orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %s", id)))
      .getPassword().equals(password);
  }
  
}
