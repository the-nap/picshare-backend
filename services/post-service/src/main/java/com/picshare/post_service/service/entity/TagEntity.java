package com.picshare.post_service.service.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class TagEntity {

  @Id
  @Nonnull
  private String tag;
  
}
