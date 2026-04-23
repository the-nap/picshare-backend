package com.picshare.userservice.service.service;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.picshare.userservice.client.UserClient;
import com.picshare.userservice.service.exceptions.UserNotFoundException;
import com.picshare.userservice.service.mapper.UserMapper;
import com.picshare.userservice.service.repository.ConnectionRepository;
import com.picshare.userservice.service.repository.UserRepository;
import com.picshare.userservice.service.dto.UserDTO;
import com.picshare.userservice.service.entity.ConnectionEntity;
import com.picshare.userservice.service.entity.UserEntity;
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

  public void uploadAvatar(String id, MultipartFile media){
    if(!this.userClient.uploadAvatar(media, id))
      throw new UploadException("Error while uploading");
  }

  public void updateBio(String id, String bio){
    UserEntity entity = this.userRepository.findById(id)
      .orElseThrow(() -> new UserNotFoundException("id", id));

    entity.setBio(bio);
    userRepository.save(entity);
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

  public String follow(String userId, String toFollowId){

    UserEntity user = userRepository.findById(userId)
      .orElseThrow(() -> new UserNotFoundException("id", userId));
    UserEntity toFollow = userRepository.findById(toFollowId)
      .orElseThrow(() -> new UserNotFoundException("id", toFollowId));

    connectionRepository.save(new ConnectionEntity(user, toFollow));
    return toFollow.getUsername();
  }

public boolean follows(String userId, String followedId){
    UserEntity follower = userRepository.findById(userId)
      .orElseThrow(() -> new UserNotFoundException("id", userId));
    UserEntity followed = userRepository.findById(followedId)
      .orElseThrow(() -> new UserNotFoundException("id", followedId));

  return connectionRepository.existsByFollowerAndFollowed(follower, followed);
}

  public String unfollow(String userId, String toUnfollowId){

    UserEntity user = userRepository.findById(userId)
      .orElseThrow(() -> new UserNotFoundException("id", userId));
    UserEntity toUnfollow = userRepository.findById(toUnfollowId)
      .orElseThrow(() -> new UserNotFoundException("id", toUnfollowId));

    connectionRepository.delete(
        connectionRepository.findByFollowerAndFollowed(user, toUnfollow));

    return toUnfollow.getUsername();
  }

}
