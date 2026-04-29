package com.picshare.userservice.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.picshare.userservice.service.entity.ConnectionEntity;
import com.picshare.userservice.service.entity.ConnectionEntity.ConnectionId;
import com.picshare.userservice.service.entity.ConnectionStatus;
import com.picshare.userservice.service.entity.UserEntity;

@Repository
public interface ConnectionRepository extends JpaRepository<ConnectionEntity, ConnectionId>{

  Streamable<ConnectionEntity> findByFollowerAndStatus(UserEntity user, ConnectionStatus status);

  Streamable<ConnectionEntity> findByFollowedAndStatus(UserEntity user, ConnectionStatus status);

  Optional<ConnectionEntity> findByFollowerAndFollowedAndStatus(UserEntity follower, UserEntity followed, ConnectionStatus status);

  long countByFollowerAndStatus(UserEntity user, ConnectionStatus status);

  long countByFollowedAndStatus(UserEntity user, ConnectionStatus status);

  boolean existsByFollowerAndFollowedAndStatus(UserEntity follower, UserEntity followed, ConnectionStatus status);

  void deleteByFollowerAndFollowedAndStatus(UserEntity follower, UserEntity followed, ConnectionStatus status);

  // Convenience methods for REGULAR status
  default Streamable<ConnectionEntity> findByFollower(UserEntity user) {
    return findByFollowerAndStatus(user, ConnectionStatus.REGULAR);
  }

  default Streamable<ConnectionEntity> findByFollowed(UserEntity user) {
    return findByFollowedAndStatus(user, ConnectionStatus.REGULAR);
  }

  default Optional<ConnectionEntity> findByFollowerAndFollowed(UserEntity follower, UserEntity followed){
    return findByFollowerAndFollowedAndStatus(follower, followed, ConnectionStatus.REGULAR);
  }

  default long countByFollower(UserEntity user) {
    return countByFollowerAndStatus(user, ConnectionStatus.REGULAR);
  }

  default long countByFollowed(UserEntity user) {
    return countByFollowedAndStatus(user, ConnectionStatus.REGULAR);
  }

  default boolean existsByFollowerAndFollowed(UserEntity follower, UserEntity followed) {
    return existsByFollowerAndFollowedAndStatus(follower, followed, ConnectionStatus.REGULAR);
  }

}
