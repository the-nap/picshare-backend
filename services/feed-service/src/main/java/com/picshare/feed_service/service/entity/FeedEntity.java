package com.picshare.feed_service.service.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

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
import lombok.RequiredArgsConstructor;

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

  @Column(name = "poster_id")
  private String posterId;

  @Column
  @Enumerated(EnumType.STRING)
  private FeedStatus status = FeedStatus.REGULAR;

  @CreationTimestamp
  @Column(name = "timestamp")
  private Date timestamp;

}
