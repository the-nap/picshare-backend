package com.picshare.userservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.picshare.userservice.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

  Optional<UserEntity> findById(String id);

  Optional<UserEntity> findByUsername(String username);

  boolean existsByUsername(String username);

  Optional<UserEntity> findByEmail(String email);

  void deleteById(String id);

  @Query(
    value = "SELECT u FROM UserEntity u WHERE u.username LIKE %?1% ORDER BY creationDate OFFSET ?2 LIMIT ?3",
    nativeQuery = true)
  List<UserEntity> searchByUsername(String username, Integer offset, Integer max);

  @Query(
    value = "SELECT u FROM UserEntity u WHERE u.email LIKE %?1% ORDER BY creationDate OFFSET ?2 LIMIT ?3",
    nativeQuery = true)
  List<UserEntity> searchByEmail(String email, Integer offset, Integer max);

}
