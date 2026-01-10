package com.picshare.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.picshare.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

  UserEntity findById(long id);
  
}
