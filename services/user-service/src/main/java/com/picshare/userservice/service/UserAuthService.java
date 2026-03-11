package com.picshare.userservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.picshare.userservice.dto.UserDTO;
import com.picshare.userservice.mapper.UserMapper;
import com.picshare.userservice.repository.UserRepository;
import com.picshare.userservice.service.exceptions.UserNotFoundException;
import com.picshare.userservice.service.exceptions.UsernameExistsException;

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

  @Transactional
  public boolean updateCredential(String id, String password){
    repository.findById(id)
      .orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %s", id)))
      .setPassword(password);
    return true;
  }
  
  @Transactional
  public UserDTO createUser(UserDTO user){
    if(repository.existsByUsername(user.getUsername()))
      throw new UsernameExistsException(String.format("User already exists with username: %s", user.getUsername()));
    repository.save(mapper.toEntity(user));

    return mapper.toDto(
        repository.findByUsername(user.getUsername())
          .orElseThrow(() -> new UserNotFoundException(String.format("User not found with username: %s", user.getUsername()))));
  }
}
