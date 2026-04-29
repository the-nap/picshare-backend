package com.picshare.feed_service.service.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.picshare.feed_service.client.FeedClient;
import com.picshare.feed_service.service.dto.FeedDto;
import com.picshare.feed_service.service.dto.PostDto;
import com.picshare.feed_service.service.dto.UpdateDto;
import com.picshare.feed_service.service.entity.FeedEntity;
import com.picshare.feed_service.service.entity.FeedStatus;
import com.picshare.feed_service.service.exceptions.FeedNotFoundException;
import com.picshare.feed_service.service.mapper.FeedMapper;
import com.picshare.feed_service.service.repository.FeedRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FeedService {

  private final FeedRepository feedRepository;
  private final FeedMapper feedMapper;
  private final FeedClient feedClient;

  public List<PostDto> getFeed(String id, int offset, int max){
    List<String> ids = feedRepository.findByUserId(id, PageRequest.of
      (offset, max, Sort.by("timestamp").descending()))
        .map(entity -> entity.getPostId())
        .get()
        .collect(Collectors.toList());
    return feedClient.getPosts(ids);
  }
  
  public void markAsSeen(String userId, String postId){
    markAsSeen(feedRepository.findByUserIdAndPostId(userId, postId)
        .orElseThrow(() -> new FeedNotFoundException(String.format("user with id: %s does not have post with id: %s in its feed", userId, postId))));
  }

  @Transactional
  public void markAsSeen(String userId){ final int max = 100;
    int offset = 0;
    Streamable<FeedEntity> entities;
    do{
      entities = this.feedRepository.findByUserId(userId, PageRequest.of(offset, max));
    entities.stream()
      .peek(entity -> markAsSeen(entity));
    offset++;
    } while(!entities.isEmpty());
  }

  @Transactional
  public void markAsSeen(FeedEntity entity){
    entity.setStatus(FeedStatus.SEEN);
    feedRepository.save(entity);
  }

  public void add(String userId, String postId){
    FeedDto feed = new FeedDto(userId, postId);
    feedRepository.save(feedMapper.toEntity(feed));
  }


  @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
  @Transactional
  public void removeOld(){
    LocalDate now = LocalDate.now();
    Date yesterday = Date.from(now.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    feedRepository.deleteAllByTimestampBefore(yesterday);
  }

  @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
  public void update(){
    List<UpdateDto> updates = this.feedClient.getUpdates();
    if(updates.isEmpty())
      return;
    Map<String,List<String>> postsByUser = updates.stream()
      .collect(Collectors.groupingBy(
            UpdateDto::getUserId,
            Collectors.mapping(UpdateDto::getPostId, Collectors.toList())
            )
          );
    handleInsertion(postsByUser);
  }

  @Transactional
  private void handleInsertion(Map<String, List<String>> postsByUser) {
    for ( Map.Entry<String,List<String>> entry : postsByUser.entrySet() ){
      String posterId = entry.getKey();
      List<String> postIds = entry.getValue();
      List<String> followers = this.feedClient.getFollowers(posterId);

      List<FeedDto> toSave = new ArrayList<>(followers.size() * postIds.size());
      for( String follower : followers )
        for( String postId : postIds) 
          toSave.add(new FeedDto(follower, postId));

      feedRepository.saveAll(toSave
          .stream()
          .map(dto -> feedMapper.toEntity(dto))
          .collect(Collectors.toList())
          );
    }
  }
}
