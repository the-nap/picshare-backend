package com.picshare.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.picshare.dto.UserDTO;
import com.picshare.mapper.UserMapper;
import com.picshare.repository.ConnectionRepository;
import com.picshare.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final ConnectionRepository connectionRepository;
  private final UserMapper userMapper;

  public Set<UserDTO> getFollowers(Long id){
    return connectionRepository.findByFollowed(userRepository.findById(id).get())
      .stream()
      .map(connection -> userMapper.toDto(connection.getFollower()))
      .collect(Collectors.toSet());
  }

  public Set<UserDTO> getFollowed(Long id){
    return connectionRepository.findByFollower(userRepository.findById(id).get())
      .stream()
      .map(connection -> userMapper.toDto(connection.getFollowed()))
      .collect(Collectors.toSet());
  }
}
