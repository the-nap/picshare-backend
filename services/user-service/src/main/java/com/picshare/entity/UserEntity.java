package com.picshare.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "users")
@Data
@Entity
public class UserEntity{

  @Id
  @Column(nullable = false)
  private String id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  @CreationTimestamp
  @Column(updatable = false)
  private Date creationDate;

  @UpdateTimestamp
  private Date updateDate;

}
