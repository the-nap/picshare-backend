package com.picshare.post_service.service.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Table(name = "posts")
@Entity
public class PostEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(nullable = false, updatable = false)
  private String id;

  @Column(nullable = false, updatable = false)
  @NonNull
  private String userId;

  @CreationTimestamp
  @Column(updatable = false)
  private Date creationDate = new Date();

  @Column(length = 140)
  @NonNull
  private String description;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToMany
  @JoinTable(
    name = "post_tags",
    joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<TagEntity> tags;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private PostStatus status = PostStatus.PENDING;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "post_likes", joinColumns = @JoinColumn(name = "post_id"))
  @Column(name = "userIds")
  private Set<String> likedBy = new HashSet<>();

  public boolean addLike(String id){
    return this.likedBy.add(id);
  }
}
