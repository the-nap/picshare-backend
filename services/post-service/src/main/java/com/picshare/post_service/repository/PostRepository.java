package com.picshare.post_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.picshare.post_service.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

  Optional<PostEntity> findById(Long id);

  List<PostEntity> findByUserId(String id);

}
