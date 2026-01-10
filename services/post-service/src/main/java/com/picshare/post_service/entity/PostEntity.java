package com.picshare.post_service.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
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
  @NonNull
  private Boolean mediaPending = true;

  @CreationTimestamp
  @Column(updatable = false)
  private Date creationDate;

  @Column(length = 140)
  @NonNull
  private String caption;
  
}
