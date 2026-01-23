package com.picshare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.picshare.entity.FeedEntity;

@Repository
public interface FeedRepository extends CrudRepository<FeedEntity, Long>{

  List<FeedEntity> findByUserIdOrderByTimestampDesc(Long id);

  Optional<FeedEntity> findByUserIdAndPostId(Long userId, Long postId);

}
