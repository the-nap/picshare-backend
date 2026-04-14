package com.picshare.userservice.service;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.picshare.userservice.client.UserClient;
import com.picshare.userservice.dto.UserDTO;
import com.picshare.userservice.entity.ConnectionEntity;
import com.picshare.userservice.entity.UserEntity;
import com.picshare.userservice.mapper.UserMapper;
import com.picshare.userservice.repository.ConnectionRepository;
import com.picshare.userservice.repository.UserRepository;
import com.picshare.userservice.service.exceptions.UserNotFoundException;
import com.picshare.userservice.service.exceptions.UploadException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final ConnectionRepository connectionRepository;
  private final UserMapper userMapper;
  private final UserClient userClient;

  public UserDTO getUser(String id){
    return userMapper.toDto(userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("id", id)),
        connectionRepository);
  }

  public void uploadAvatar(String id, InputStream stream){
    if(!this.userClient.uploadAvatar(stream, id))
      throw new UploadException("Error while uploading");
  }

  public UserDTO getByUsername(String username){
    return userMapper.toDto(userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("username", username)),
        connectionRepository);
  }

  public List<UserDTO> search(String toSearch, int offset, int max){
    return this.userRepository.searchByEmailOrUsername(toSearch, offset, max)
      .stream()
      .map((entity) -> userMapper.toDto(entity, connectionRepository))
      .collect(Collectors.toList());
  }

  public void follow(String userId, String toFollowId){

    UserEntity user = userRepository.findById(userId)
      .orElseThrow(() -> new UserNotFoundException("id", userId));
    UserEntity toFollow = userRepository.findById(toFollowId)
      .orElseThrow(() -> new UserNotFoundException("id", toFollowId));

    connectionRepository.save(new ConnectionEntity(user, toFollow));
  }

  public void unfollow(String userId, String toUnfollowId){

    UserEntity user = userRepository.findById(userId)
      .orElseThrow(() -> new UserNotFoundException("id", userId));
    UserEntity toUnfollow = userRepository.findById(toUnfollowId)
      .orElseThrow(() -> new UserNotFoundException("id", toUnfollowId));

    connectionRepository.delete(connectionRepository.findByFollowerAndFollowed(user, toUnfollow));
  }
}
