package com.picshare.userservice.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
  @MapsId("follower")
  private UserEntity follower;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "followed_id")
  @MapsId("followed")
  private UserEntity followed;

  @Data
  @NoArgsConstructor(force = true)
  @RequiredArgsConstructor
  public static class ConnectionId implements Serializable {
    private final String follower;
    private final String followed;
  }
}
