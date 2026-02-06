package com.picshare.userservice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.picshare.userservice.entity.ConnectionEntity;
import com.picshare.userservice.entity.ConnectionEntity.ConnectionId;
import com.picshare.userservice.entity.UserEntity;

@Repository
public interface ConnectionRepository extends CrudRepository<ConnectionEntity, ConnectionId>{

  // Find all users followed by this
  List<ConnectionEntity> findByFollower(UserEntity follower);
  
  // Find all users this follows
  List<ConnectionEntity> findByFollowed(UserEntity followed);

  ConnectionEntity findByFollowerAndFollowed(UserEntity follower, UserEntity followed);

  // Check if follower follows followed
  boolean existsByFollowerAndFollowed(UserEntity follower, UserEntity followed);

  // Delete the relationship
  void deleteByFollowerAndFollowed(UserEntity follower, UserEntity followed);
}
