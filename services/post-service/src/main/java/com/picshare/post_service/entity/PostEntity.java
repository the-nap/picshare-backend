package com.picshare.post_service.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
@Entity
public class PostEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, updatable = false)
  private Long id;

  @Column(nullable = false, updatable = false)
  @NonNull
  private Long userId;

  @Column
  private String imageUrl;

  @CreationTimestamp
  @Column(updatable = false)
  private Date creationDate;

  @Column(length = 140)
  @NonNull
  private String description;

  @Column(name = "tags")
  private List<String> tags;
  
  @Column(name = "status",nullable = false)
  @Enumerated(EnumType.STRING)
  private PostStatus status;


  public enum PostStatus{
    PENDING,
    PUBLISHED,
    FAILED
  }
}
