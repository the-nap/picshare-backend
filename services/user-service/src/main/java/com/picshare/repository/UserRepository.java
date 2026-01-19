package com.picshare.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.picshare.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

  Optional<UserEntity> findById(Long id);

  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);

}
