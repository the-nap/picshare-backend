package com.picshare.post_service.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.picshare.post_service.service.entity.TagEntity;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, String>{

  Optional<TagEntity> findByTagName(String tagName);
  
}
