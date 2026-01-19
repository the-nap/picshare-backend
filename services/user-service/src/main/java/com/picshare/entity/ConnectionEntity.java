package com.picshare.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "connections")
@Data
@Entity
public class ConnectionEntity {

  @EmbeddedId
  private ConnectionId id;

  @ManyToOne
  @MapsId("followerId")
  private UserEntity follower;

  @ManyToOne
  @MapsId("followedId")
  private UserEntity followed;

  @Data
  @Embeddable
  public static class ConnectionId implements Serializable {
    private Long followerId;
    private Long followedId;
  }
}
