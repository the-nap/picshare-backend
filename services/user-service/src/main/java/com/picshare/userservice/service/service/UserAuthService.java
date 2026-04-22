package com.picshare.userservice.service.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.picshare.userservice.service.dto.AuthUserDTO;
import com.picshare.userservice.service.exceptions.UserNotFoundException;
import com.picshare.userservice.service.exceptions.UsernameExistsException;
import com.picshare.userservice.service.mapper.AuthMapper;
import com.picshare.userservice.service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAuthService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository repository;
  private final AuthMapper mapper;

  public AuthUserDTO getById(String id){
    return repository.findById(id)
      .map(mapper::toDto)
      .orElse(null);
  }

  public AuthUserDTO getByUsername(String username){
    return repository.findByUsername(username)
      .map(mapper::toDto)
      .orElse(null);
  }

  public AuthUserDTO getByEmail(String email){
    return repository.findByEmail(email)
      .map(mapper::toDto)
      .orElse(null);
  }

  public List<AuthUserDTO> searchByEmail(String email, Integer first, Integer max){
    return repository.searchByEmail(email, first, max)
      .stream()
      .map(entity -> mapper.toDto(entity))
      .toList();
  }

  public List<AuthUserDTO> searchByUsername(String username, Integer first, Integer max){
    return repository.searchByUsername(username, first, max)
      .stream()
      .map(entity -> mapper.toDto(entity))
      .toList();
  }

  public boolean checkPassword(String id, String password){
    return passwordEncoder.matches(password,
        repository.findById(id)
        .orElseThrow(() ->  new UserNotFoundException("id", id))
        .getPassword());

  }

  public List<AuthUserDTO> getAll(Integer first, Integer max){
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
    
    entity.setPassword(passwordEncoder.encode(password));

    repository.save(entity);
    return true;
  }
  
  @Transactional
  public AuthUserDTO createId(String username){
    if(repository.existsByUsername(username))
      throw new UsernameExistsException(String.format("User already exists with username: %s", username));
    AuthUserDTO user = new AuthUserDTO();
    user.setUsername(username);
    user.setEmail(String.format("%s@default.com",username));
    return mapper.toDto(repository.save(mapper.toEntity(user)));

  }

  @Transactional
  public boolean updateUser(AuthUserDTO user){
    UserEntity entity = repository.findById(user.getId())
     .orElseThrow(() -> new UserNotFoundException("id", user.getId()));

    entity.setUsername(user.getUsername());
    entity.setEmail(user.getEmail());
    
    repository.save(entity);
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
