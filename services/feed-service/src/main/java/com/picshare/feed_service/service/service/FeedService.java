package com.picshare.feed_service.service.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.picshare.feed_service.service.dto.UpdateRequest;
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

  @Transactional(readOnly = true)
  public List<PostDto> getFeed(String id, int offset, int max){
    List<String> ids = feedRepository.findByUserId(id, PageRequest.of
      (offset, max, Sort.by("timestamp").descending()))
        .map(entity -> entity.getPostId())
        .get()
        .collect(Collectors.toList());
    return feedClient.getPosts(ids);
  }
  
  @Transactional(readOnly = true)
  public void postSeen(String userId, String postId){
    postSeen(feedRepository.findByUserIdAndPostId(userId, postId)
        .orElseThrow(() -> new FeedNotFoundException(String.format("user with id: %s does not have post with id: %s in its feed", userId, postId))));
  }

  @Transactional
  public void userDeleted(String userId){
    final int max = 999;
    final int offset = 0;
    Streamable<FeedEntity> entities;
    do{
      entities = this.feedRepository.findByUserId(userId, PageRequest.of(offset, max));
      entities.stream()
        .forEach(this::markForDeletion);

      feedRepository.saveAll(entities);

      //offset not incremented because findByUserId only returns REGULAR status entities
    } while(!entities.isEmpty());
  }
  
  @Transactional
  public void postDeleted(String postId){
    final int max = 999;
    final int offset = 0;
    Streamable<FeedEntity> entities;
    do{
      entities = this.feedRepository.findByPostId(postId, PageRequest.of(offset,max));
      entities.stream()
        .forEach(this::markForDeletion);

      feedRepository.saveAll(entities);

      //offset not incremented because findByUserId only returns REGULAR status entities
    } while(!entities.isEmpty());
  }

  @Transactional
  public void postSeen(FeedEntity entity){
    entity.setStatus(FeedStatus.SEEN);
    feedRepository.save(entity);
  }

  public void add(String userId, String postId){
    FeedDto feed = new FeedDto(userId, postId);
    feedRepository.save(feedMapper.toEntity(feed));
  }

  @Transactional
  private void markForDeletion(FeedEntity entity){
    entity.setStatus(FeedStatus.DELETED);
  }

  @Transactional
  public void connectionCreated(String followerId, String followedId){
    UpdateRequest request = new UpdateRequest(followedId, getYesterday());
    Map<String, String> posts = feedClient.getPostsForNewConnection(request);

    Set<FeedEntity> entities = posts.entrySet()
      .stream()
      .map(entry -> {
        FeedEntity toAdd = new FeedEntity();
        toAdd.setUserId(followerId);
        toAdd.setPostId(entry.getKey());
        toAdd.setPosterId(entry.getValue());
        return toAdd;
      })
    .collect(Collectors.toSet());

    feedRepository.saveAll(entities);
  }


  @Transactional
  public void connectionDeleted(String followerId, String followedId){
    Streamable<FeedEntity> entities = feedRepository.findByUserIdAndPosterId(followerId, followedId);

    entities.stream()
      .forEach(this::markForDeletion);

    feedRepository.saveAll(entities);
  }

  @Transactional
  public void postConfirmed(String postId, String posterId){
    Set<FeedEntity> entities = new HashSet<>();

    List<String> followersId = feedClient.getFollowers(posterId);

    followersId.forEach(
      followerId -> {
        FeedEntity entity = new FeedEntity();
        entity.setPostId(postId);
        entity.setUserId(followerId);
        entity.setPosterId(posterId);
        entities.add(entity);
      });

    feedRepository.saveAll(entities);
  }

  @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
  @Transactional
  public void removeOld(){
    Date yesterday = getYesterday();
    feedRepository.deleteAllByTimestampBefore(yesterday);
  }

  @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
  @Transactional
  public void removeSeenOrDeleted(){
    feedRepository.deleteAllByStatus(FeedStatus.DELETED);
  }

  private Date getYesterday() {
    Date result = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
    return result;
  }
}
