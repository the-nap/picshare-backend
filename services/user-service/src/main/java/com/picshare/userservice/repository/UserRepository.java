package com.picshare.userservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    value = "SELECT * FROM users u WHERE u.username LIKE %?1% ORDER BY creation_date OFFSET ?2 LIMIT ?3",
    nativeQuery = true)
  List<UserEntity> searchByUsername(String username, Integer offset, Integer max);

  @Query(
    value = "SELECT * FROM users u WHERE u.email LIKE %?1% ORDER BY creation_date OFFSET ?2 LIMIT ?3",
    nativeQuery = true)
  List<UserEntity> searchByEmail(String email, Integer offset, Integer max);

  @Query(
    value = "SELECT * FROM users u WHERE u.email LIKE %?1% OR u.username LIKE %?1% ORDER BY creation_date OFFSET ?2 LIMIT ?3",
    nativeQuery = true)
  List<UserEntity> searchByEmailOrUsername(String toSearch, Integer offset, Integer max);

  @Query(
    value = "SELECT * FROM users u WHERE u.username LIKE %?1%",
    nativeQuery = true)
  Set<UserEntity> countByUsername(String username);

  @Query(
    value = "SELECT * FROM users u WHERE u.email LIKE %?1%",
    nativeQuery = true)
  Set<UserEntity> countByEmail(String email);

  @Query(
    value = "SELECT * FROM users u ORDER BY creation_date OFFSET ?1 LIMIT ?2",
    nativeQuery = true)
  Set<UserEntity> getAll(Integer first, Integer max);
}
