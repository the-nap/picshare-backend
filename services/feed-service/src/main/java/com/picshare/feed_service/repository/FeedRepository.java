package com.picshare.feed_service.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.picshare.feed_service.entity.FeedEntity;

@Repository
public interface FeedRepository extends CrudRepository<FeedEntity, Long>{

  List<FeedEntity> findByUserIdOrderByTimestampDesc(String id);

  Optional<FeedEntity> findByUserIdAndPostId(String userId, Long postId);

  void deleteAllBySeenAtAfter(Date date);

}
