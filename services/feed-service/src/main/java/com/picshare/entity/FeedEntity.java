package com.picshare.entity;

import java.security.Timestamp;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "feeds")
@Data
@Entity
public class FeedEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "user")
  private Long userId;

  @Column(name = "post")
  private Long postId;

  @CreationTimestamp
  @Column(name = "timestamp")
  private Date createdAt;

}
