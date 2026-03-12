package com.picshare.userservice.service;

import java.util.List;

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
      .orElse(null);
  }

  public UserDTO getByUsername(String username){
    return repository.findByUsername(username)
      .map(mapper::toDto)
      .orElse(null);
  }

  public UserDTO getByEmail(String email){
    return repository.findByEmail(email)
      .map(mapper::toDto)
      .orElse(null);
  }

  public List<UserDTO> searchByEmail(String email, Integer first, Integer max){
    return repository.searchByEmail(email, first, max)
      .stream()
      .map(entity -> mapper.toDto(entity))
      .toList();
  }

  public List<UserDTO> searchByUsername(String username, Integer first, Integer max){
    return repository.searchByUsername(username, first, max)
      .stream()
      .map(entity -> mapper.toDto(entity))
      .toList();
  }

  public boolean checkPassword(String id, String password){
    return repository.findById(id)
      .orElseThrow(() -> new UserNotFoundException("id", id))
      .getPassword().equals(password);
  }

  @Transactional
  public boolean updateCredential(String id, String password){
    repository.findById(id)
      .orElseThrow(() -> new UserNotFoundException("id", id))
      .setPassword(password);
    return true;
  }
  
  @Transactional
  public UserDTO createUser(UserDTO user){
    if(repository.existsByUsername(user.getUsername()))
      throw new UsernameExistsException(String.format("User already exists with username: %s", user.getUsername()));
    return mapper.toDto(repository.save(mapper.toEntity(user)));

  }

  @Transactional
  public boolean deleteUser(String id){
    if(!repository.existsById(id))
      throw new UserNotFoundException("id", id);
    repository.deleteById(id);
    return true;
  }

}
