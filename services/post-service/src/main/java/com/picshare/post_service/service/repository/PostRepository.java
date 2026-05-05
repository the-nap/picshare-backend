package com.picshare.post_service.service.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.picshare.post_service.service.entity.PostEntity;
import com.picshare.post_service.service.entity.PostStatus;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {

  Optional<PostEntity> findByIdAndStatus(String id, PostStatus status);

  Streamable<PostEntity> findByUserIdAndCreationDateAfterAndStatus(String userId, Date date, PostStatus status);

  Streamable<PostEntity> findByUserIdAndStatus(String id, Pageable pageable, PostStatus status);

  Streamable<PostEntity> findAllByStatus(PostStatus status);

  default Optional<PostEntity> findById(String id){
    return this.findByIdAndStatus(id, PostStatus.CONFIRMED);
  }

  default Streamable<PostEntity> findByUserIdAndCreationDateAfter(String userId, Date date){
    return this.findByUserIdAndCreationDateAfterAndStatus(userId, date, PostStatus.CONFIRMED);
  }

  default Streamable<PostEntity> findByUserId(String id, Pageable pageable){
    return this.findByUserIdAndStatus(id, pageable, PostStatus.CONFIRMED);
  }

  @Query("SELECT p FROM PostEntity p JOIN p.tags t WHERE t.tagName LIKE :tag AND p.status='CONFIRMED'")
  Page<PostEntity> findByTag(@Param("tag") String tag, Pageable pageable);

  default Page<PostEntity> findByTagContaining(String tag, Pageable pageable){
    return this.findByTag('%'+tag+'%', pageable);
  }

  void deleteAllByStatus(PostStatus status);

}
