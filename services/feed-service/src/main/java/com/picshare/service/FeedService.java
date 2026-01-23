package com.picshare.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.picshare.DTO.FeedDto;
import com.picshare.DTO.UpdateDto;
import com.picshare.mapper.FeedMapper;
import com.picshare.repository.FeedRepository;
import com.picshare.client.FeedClient;
import com.picshare.entity.FeedEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FeedService {

  private final FeedRepository feedRepository;
  private final FeedMapper feedMapper;
  private final FeedClient feedClient;

  public List<Long> getFeed(Long id){
    return feedRepository.findByUserIdOrderByTimestampDesc(id)
      .stream()
      .map(entity -> entity.getPostId())
      .collect(Collectors.toList());
  }
  
  public void markAsSeen(Long userId, Long postId){
    Optional<FeedEntity> seen = feedRepository.findByUserIdAndPostId(userId, postId);
    if(seen.isEmpty()){
      // To Do
    }
    FeedEntity entity = seen.get();
    entity.setSeenAt(new Date());
    feedRepository.save(entity);
  }

  public void add(Long userId, Long postId){
    FeedDto feed = new FeedDto(userId, postId);
    feedRepository.save(feedMapper.toEntity(feed));
  }

  public void update(){
    List<UpdateDto> updates = this.feedClient.getUpdates();
    if(updates.isEmpty())
      return;
    Map<Long,List<Long>> postsByUser = updates.stream()
      .collect(Collectors.groupingBy(
            UpdateDto::getUserId,
            Collectors.mapping(UpdateDto::getPostId, Collectors.toList())
            )
          );
    handleInsertion(postsByUser);
  }

  private void handleInsertion(Map<Long, List<Long>> postsByUser) {
    for ( Map.Entry<Long,List<Long>> entry : postsByUser.entrySet() ){
      Long posterId = entry.getKey();
      List<Long> postIds = entry.getValue();
      List<Long> followers = this.feedClient.getFollowers(posterId);

      List<FeedDto> toSave = new ArrayList<>(followers.size() * postIds.size());
      for( Long follower : followers )
        for( Long postId : postIds) 
          toSave.add(new FeedDto(follower, postId));

      feedRepository.saveAll(toSave
          .stream()
          .map(dto -> feedMapper.toEntity(dto))
          .collect(Collectors.toList())
          );
    }
  }
}
