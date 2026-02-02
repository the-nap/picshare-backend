package com.picshare.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Table(name = "connections")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@IdClass(ConnectionEntity.ConnectionId.class)
public class ConnectionEntity {

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "follower_id")
  @MapsId("followerId")
  private UserEntity follower;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "followed_id")
  @MapsId("followedId")
  private UserEntity followed;

  @Data
  @NoArgsConstructor(force = true)
  @RequiredArgsConstructor
  public static class ConnectionId implements Serializable {
    private final String followerId;
    private final String followedId;
  }
}
