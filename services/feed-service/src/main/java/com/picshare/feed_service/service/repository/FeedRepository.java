package com.picshare.feed_service.service.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.picshare.feed_service.service.entity.FeedEntity;
import com.picshare.feed_service.service.entity.FeedStatus;

@Repository
public interface FeedRepository extends CrudRepository<FeedEntity, String>{

  Streamable<FeedEntity> findByUserId(String userId, FeedStatus status, Pageable pageable);

  Streamable<FeedEntity> findByPostId(String postId, FeedStatus status, Pageable pageable);

  Optional<FeedEntity> findByUserIdAndPostId(String userId, String postId, FeedStatus status);

  void deleteAllByTimestampBefore(Date date);

  void deleteAllByStatus(FeedStatus status);


  default Streamable<FeedEntity> findByUserId(String userId, Pageable pageable){
    return this.findByUserId(userId, FeedStatus.REGULAR, pageable);
  }

  default Streamable<FeedEntity> findByPostId(String postId, Pageable pageable){
    return this.findByUserId(postId, FeedStatus.REGULAR, pageable);
  }

  default Optional<FeedEntity> findByUserIdAndPostId(String userId, String postId){
    return this.findByUserIdAndPostId(userId, postId, FeedStatus.REGULAR);
  }
}
