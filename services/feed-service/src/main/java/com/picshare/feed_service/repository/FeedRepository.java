package com.picshare.feed_service.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.picshare.feed_service.entity.FeedEntity;

@Repository
public interface FeedRepository extends CrudRepository<FeedEntity, String>{

  Streamable<FeedEntity> findByUserId(String userId, Pageable pageable);

  Optional<FeedEntity> findByUserIdAndPostId(String userId, String postId);

  void deleteAllBySeenAtAfter(Date date);

}
