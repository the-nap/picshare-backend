package com.picshare.userservice.service.service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.picshare.userservice.client.UserClient;
import com.picshare.userservice.event.UserEventProducer;
import com.picshare.userservice.service.dto.UserDTO;
import com.picshare.userservice.service.entity.ConnectionEntity;
import com.picshare.userservice.service.entity.ConnectionStatus;
import com.picshare.userservice.service.entity.UserEntity;
import com.picshare.userservice.service.entity.UserStatus;
import com.picshare.userservice.service.exceptions.ConnectionNotFoundException;
import com.picshare.userservice.service.exceptions.UserNotFoundException;
import com.picshare.userservice.service.mapper.UserMapper;
import com.picshare.userservice.service.repository.ConnectionRepository;
import com.picshare.userservice.service.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final ConnectionRepository connectionRepository;
  private final UserMapper userMapper;
  private final UserClient userClient;
  private final UserEventProducer eventProducer;

  public UserDTO getUser(String id){
    return userMapper.toDto(userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("id", id)),
        connectionRepository);
  }

  public void uploadAvatar(String id, MultipartFile media){
    this.userClient.uploadAvatar(media, id);
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

  @Transactional
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

  @Transactional
  public String unfollow(String userId, String toUnfollowId){

    UserEntity user = userRepository.findById(userId)
      .orElseThrow(() -> new UserNotFoundException("id", userId));
    UserEntity toUnfollow = userRepository.findById(toUnfollowId)
      .orElseThrow(() -> new UserNotFoundException("id", toUnfollowId));

    ConnectionEntity connection = connectionRepository.findByFollowerAndFollowed(user, toUnfollow)
      .orElseThrow(() -> new ConnectionNotFoundException(String.format("%s is not following %s", user.getUsername(), toUnfollow.getUsername())));

    connection.setStatus(ConnectionStatus.DELETED);
    connectionRepository.save(connection);

    return toUnfollow.getUsername();
  }

  @Transactional
  private void removeUserConnections(UserEntity user){
    Streamable<ConnectionEntity> entities = this.connectionRepository.findByFollowed(user).and(this.connectionRepository.findByFollower(user));
    entities.stream()
      .peek(entity -> entity.setStatus(ConnectionStatus.DELETED));
    connectionRepository.saveAll(entities);
  }

  @Transactional
  public void deleteUser(String userId){
    UserEntity entity = this.userRepository.findById(userId)
      .orElseThrow(() -> new UserNotFoundException("id", userId));

    entity.setStatus(UserStatus.DELETED);
    removeUserConnections(entity);

    userRepository.save(entity);
    eventProducer.sendUserDeletedEvent(userId);
  }

  @Transactional
  @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
  private void removeDeleted() {
    this.userRepository.deleteAllByStatus(UserStatus.DELETED);
  }

}
