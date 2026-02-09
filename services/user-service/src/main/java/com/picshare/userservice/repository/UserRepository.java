package com.picshare.userservice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.picshare.userservice.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {

  Optional<UserEntity> findById(String id);

  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);

  void deleteById(String id);

}
