package com.picshare.userservice.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.picshare.userservice.dto.UserDTO;
import com.picshare.userservice.entity.UserEntity;
import com.picshare.userservice.mapper.UserMapper;
import com.picshare.userservice.repository.UserRepository;
import com.picshare.userservice.service.exceptions.UserNotFoundException;
import com.picshare.userservice.service.exceptions.UsernameExistsException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAuthService {

  private final PasswordEncoder passwordEncoder;
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

  public List<UserDTO> getAll(Integer first, Integer max){
    return repository.getAll(first, max)
      .stream()
      .map(entity -> mapper.toDto(entity))
      .collect(Collectors.toList());
  }

  public Integer count(String toSearch){
    if (toSearch != null && !toSearch.isEmpty()){
      Set<UserEntity> result = repository.countByUsername(toSearch);
      result.addAll(repository.countByEmail(toSearch));
      return result.size();
    }
    long result = repository.count();
    if (result > Integer.MAX_VALUE)
      return Integer.MAX_VALUE;
    return (int) result;
  }

  @Transactional
  public boolean updateCredential(String id, String password){
    UserEntity entity = repository.findById(id)
      .orElseThrow(() -> new UserNotFoundException("id", id));

    log.info("Found user: {}", entity.getId());
    log.info("Old password: {}", entity.getPassword());
    
    String encodedPassword = passwordEncoder.encode(password);
    entity.setPassword(encodedPassword);
    log.info("New password (encoded): {}", entity.getPassword());

    repository.save(entity);
    repository.flush();
    log.info("Flushed");
    
    return true;
  }
  
  @Transactional
  public UserDTO createId(String username){
    if(repository.existsByUsername(username))
      throw new UsernameExistsException(String.format("User already exists with username: %s", username));
    UserDTO user = new UserDTO();
    user.setUsername(username);
    user.setEmail(String.format("%s@default.com",username));
    return mapper.toDto(repository.save(mapper.toEntity(user)));

  }

  @Transactional
  public boolean updateUser(UserDTO user){
    if(!repository.existsById(user.getId()))
      return false;
    repository.save(mapper.toEntity(user));
    return true;
  }

  @Transactional
  public boolean deleteUser(String id){
    if(!repository.existsById(id))
      throw new UserNotFoundException("id", id);
    repository.deleteById(id);
    return true;
  }

}
