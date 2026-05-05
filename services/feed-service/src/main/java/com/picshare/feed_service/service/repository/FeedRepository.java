package com.picshare.feed_service.service.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.picshare.feed_service.service.entity.FeedEntity;
import com.picshare.feed_service.service.entity.FeedStatus;

@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, String>{

  Streamable<FeedEntity> findByUserIdAndStatus(String userId, FeedStatus status, Pageable pageable);

  Streamable<FeedEntity> findByPostIdAndStatus(String postId, FeedStatus status, Pageable pageable);

  Streamable<FeedEntity> findByUserIdAndPosterIdAndStatus(String userId, String posterId, FeedStatus status);

  Optional<FeedEntity> findByUserIdAndPostIdAndStatus(String userId, String postId, FeedStatus status);

  void deleteAllByTimestampBefore(Date date);

  void deleteAllByStatus(FeedStatus status);

  default Streamable<FeedEntity> findByUserId(String userId, Pageable pageable){
    return this.findByUserIdAndStatus(userId, FeedStatus.REGULAR, pageable);
  }

  default Streamable<FeedEntity> findByPostId(String postId, Pageable pageable){
    return this.findByPostIdAndStatus(postId, FeedStatus.REGULAR, pageable);
  }

  default Streamable<FeedEntity> findByUserIdAndPosterId(String userId, String posterId){
    return this.findByUserIdAndPosterIdAndStatus(userId, posterId, FeedStatus.REGULAR);
  }

  default Optional<FeedEntity> findByUserIdAndPostId(String userId, String postId){
    return this.findByUserIdAndPostIdAndStatus(userId, postId, FeedStatus.REGULAR);
  }

}
