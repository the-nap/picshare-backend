package com.picshare.userservice.service.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "users")
@NoArgsConstructor
@Data
@Entity
public class UserEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(nullable = false)
  private String id;

  @Column(nullable = false)
  private String password = "default";

  @Column(unique = true, nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  @Column
  private String bio;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private UserStatus status = UserStatus.REGULAR;

  @CreationTimestamp
  @Column(updatable = false)
  private Date creationDate;

  @UpdateTimestamp
  private Date updateDate;

}
