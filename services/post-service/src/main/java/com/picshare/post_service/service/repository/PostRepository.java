package com.picshare.post_service.service.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.picshare.post_service.service.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {

  Optional<PostEntity> findById(String id);

  Streamable<PostEntity> findByUserIdAndCreationDateAfter(String userId, Date date);

  Streamable<PostEntity> findByUserId(String id, Pageable pageable);

  @Query("SELECT pe FROM PostEntity pe JOIN pe.tags t WHERE t = :tag")
  Streamable<PostEntity> findByTag(@Param("tag") String tag, Pageable pageable);

}
