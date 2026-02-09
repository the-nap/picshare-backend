package com.picshare.post_service.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "pending_updates")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateEntity {

  @EmbeddedId
  private UpdateId id;

  @Enumerated(EnumType.STRING)
  private UpdateStatus status;
  
  @Embeddable
  @Data
  public static class UpdateId implements Serializable{
    private Long postId;
    private String userId;
  }

  public enum UpdateStatus {
    PENDING,
    UPDATED
  }
}
