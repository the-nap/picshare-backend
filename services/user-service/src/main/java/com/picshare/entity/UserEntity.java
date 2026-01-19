package com.picshare.entity;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

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

  @ManyToMany
  @JoinTable(name = "follows", 
    joinColumns = @JoinColumn(name = "follower_id"),
    inverseJoinColumns = @JoinColumn(name = "followed_id"))
  private Set<UserEntity> following;

  @ManyToMany(mappedBy = "following")
  private Set<UserEntity> followers;

  @CreationTimestamp
  @Column(updatable = false)
  private Date creationDate;

  @UpdateTimestamp
  private Date updateDate;

  @Column(nullable = false)
  private Date lastAccess;

}
