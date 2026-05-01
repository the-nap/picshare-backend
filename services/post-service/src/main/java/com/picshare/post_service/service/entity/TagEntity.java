package com.picshare.post_service.service.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Table(name = "tags")
@Data
public class TagEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false, updatable = false, unique = true)
  private String tagName;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToMany(mappedBy = "tags")
  private Set<PostEntity> posts;
  

  public TagEntity(String tagName){
    this.tagName = tagName;
    this.posts = new HashSet<>();

  }

  public void addPost(PostEntity post){
    this.posts.add(post);
  }
}
