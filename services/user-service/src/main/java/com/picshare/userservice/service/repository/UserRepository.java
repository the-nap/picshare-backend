package com.picshare.userservice.service.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.picshare.userservice.service.entity.UserEntity;
import com.picshare.userservice.service.entity.UserStatus;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

  Optional<UserEntity> findByIdAndStatus(String id, UserStatus status);

  Optional<UserEntity> findByUsernameAndStatus(String username, UserStatus status);

  boolean existsByUsernameAndStatus(String username, UserStatus status);

  Optional<UserEntity> findByEmailAndStatus(String email, UserStatus status);

  void deleteByIdAndStatus(String id, UserStatus status);

  void deleteAllByStatus(UserStatus status);

  default Optional<UserEntity> findAlwaysById(String id){
    return findByIdAndStatus(id, UserStatus.DELETED).or(() -> findByIdAndStatus(id, UserStatus.REGULAR));
  }

  default Optional<UserEntity> findAlwaysByUsername(String username){
    return findByUsernameAndStatus(username, UserStatus.DELETED).or(() -> findByUsernameAndStatus(username, UserStatus.REGULAR));
  }

  default Optional<UserEntity> findAlwaysByEmail(String email){
    return findByEmailAndStatus(email, UserStatus.DELETED).or(() -> findByEmailAndStatus(email, UserStatus.REGULAR));
  }

  @Query(
    value = "SELECT * FROM users u WHERE u.username LIKE %?1% AND u.status = 'REGULAR' ORDER BY creation_date OFFSET ?2 LIMIT ?3", 
    nativeQuery = true)
  List<UserEntity> searchByUsername(String username, Integer offset, Integer max);

  @Query(
    value = "SELECT * FROM users u WHERE u.email LIKE %?1% AND u.status = 'REGULAR' ORDER BY creation_date OFFSET ?2 LIMIT ?3",
    nativeQuery = true)
  List<UserEntity> searchByEmail(String email, Integer offset, Integer max);

  @Query(
    value = "SELECT * FROM users u WHERE u.email LIKE %?1% OR u.username LIKE %?1% AND u.status = 'REGULAR' ORDER BY creation_date OFFSET ?2 LIMIT ?3",
    nativeQuery = true)
  List<UserEntity> searchByEmailOrUsername(String toSearch, Integer offset, Integer max);

  @Query(
    value = "SELECT * FROM users u WHERE u.username LIKE %?1% AND u.status = 'REGULAR'",
    nativeQuery = true)
  Set<UserEntity> countByUsername(String username);

  @Query(
    value = "SELECT * FROM users u WHERE u.email LIKE %?1% AND u.status = 'REGULAR'",
    nativeQuery = true)
  Set<UserEntity> countByEmail(String email);

  @Query(
    value = "SELECT * FROM users u AND u.status = 'REGULAR' ORDER BY creation_date OFFSET ?1 LIMIT ?2",
    nativeQuery = true)
  Set<UserEntity> getAll(Integer first, Integer max);


  default Optional<UserEntity> findById(String id){
    return this.findByIdAndStatus(id, UserStatus.REGULAR);
  }

  default Optional<UserEntity> findByUsername(String username){
    return this.findByUsernameAndStatus(username, UserStatus.REGULAR);
  }

  default boolean existsByUsername(String username){
    return this.existsByUsernameAndStatus(username, UserStatus.REGULAR);
  }

  default Optional<UserEntity> findByEmail(String email){
    return this.findByEmailAndStatus(email, UserStatus.REGULAR);
  }

  default void deleteById(String id){
    this.deleteByIdAndStatus(id, UserStatus.REGULAR);
  }

}
