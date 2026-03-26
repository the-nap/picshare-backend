package com.picshare.post_service.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class PostEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(nullable = false, updatable = false)
  private String id;

  @Column(nullable = false, updatable = false)
  @NonNull
  private String userId;

  @Column
  private String imageUrl;

  @CreationTimestamp
  @Column(updatable = false)
  private Date creationDate;

  @Column(length = 140)
  @NonNull
  private String description;

  @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
  @CollectionTable(name = "entity_tags", joinColumns = @JoinColumn(name = "entity_id"))
  @Column(name = "tag")
  private List<String> tags;
  
  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private PostStatus status;


  public enum PostStatus{
    PENDING,
    PUBLISHED,
    FAILED
  }
}
