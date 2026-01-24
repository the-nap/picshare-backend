package com.picshare.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.picshare.entity.ConnectionEntity;
import com.picshare.entity.UserEntity;
import com.picshare.entity.ConnectionEntity.ConnectionId;

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
