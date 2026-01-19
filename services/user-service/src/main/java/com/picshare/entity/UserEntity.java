package com.picshare.entity;

import java.util.Date;
import java.util.Set;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "users")
@Data
@Entity
public class UserEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  @OneToMany(mappedBy = "follower")
  private Set<ConnectionEntity> following;

  @OneToMany(mappedBy = "followed")
  private Set<ConnectionEntity> followers;

  @CreationTimestamp
  @Column(updatable = false)
  private Date creationDate;

  @UpdateTimestamp
  private Date updateDate;

  @Column(nullable = false)
  private Date lastAccess;

  public Set<UserEntity> getFollowers(){
    return followers.stream()
      .map(connection -> connection.getFollower())
      .collect(Collectors.toSet());
  }
  
  public Set<UserEntity> getFollowed(){
    return following.stream()
      .map(connection -> connection.getFollowed())
      .collect(Collectors.toSet());
  }

}
