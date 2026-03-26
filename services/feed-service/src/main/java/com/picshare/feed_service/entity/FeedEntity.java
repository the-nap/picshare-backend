package com.picshare.feed_service.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "feeds")
@Data
@NoArgsConstructor
@Entity
public class FeedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "post_id")
  private String postId;

  @UpdateTimestamp
  @Column(name = "seen_at", nullable = true)
  private Date seenAt;

  @CreationTimestamp
  @Column(name = "timestamp")
  private Date timestamp;

}
