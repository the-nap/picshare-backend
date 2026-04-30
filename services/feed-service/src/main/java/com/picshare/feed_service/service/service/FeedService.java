package com.picshare.feed_service.service.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
import com.picshare.feed_service.service.dto.FollowersRequest;
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

  public List<PostDto> getFeed(String id, int offset, int max){
    List<String> ids = feedRepository.findByUserId(id, PageRequest.of
      (offset, max, Sort.by("timestamp").descending()))
        .map(entity -> entity.getPostId())
        .get()
        .collect(Collectors.toList());
    return feedClient.getPosts(ids);
  }
  
  public void postSeen(String userId, String postId){
    postSeen(feedRepository.findByUserIdAndPostId(userId, postId)
        .orElseThrow(() -> new FeedNotFoundException(String.format("user with id: %s does not have post with id: %s in its feed", userId, postId))));
  }

  @Transactional
  public void userDeleted(String userId){
    final int max = 100;
    int offset = 0;
    Streamable<FeedEntity> entities;
    do{
      entities = this.feedRepository.findByUserId(userId, PageRequest.of(offset, max));
      entities.stream()
        .peek(entity -> markForDeletion(entity));

      feedRepository.saveAll(entities);
      offset++;

    } while(!entities.isEmpty());
  }
  
  @Transactional
  public void postDeleted(String postId){
    final int max = 100;
    int offset = 0;
    Streamable<FeedEntity> entities;
    do{
      entities = this.feedRepository.findByPostId(postId, PageRequest.of(offset,max));
      entities.stream()
        .peek(entity -> markForDeletion(entity));

      feedRepository.saveAll(entities);
      offset++;

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

  public void markForDeletion(FeedEntity entity){
    entity.setStatus(FeedStatus.DELETED);
  }

  @Transactional
  public void connectionCreated(String followerId, String followedId){
    LocalDate now = LocalDate.now();
    Date yesterday = Date.from(now.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    Set<FeedEntity> entities = new HashSet<>();

    int offset = 0;
    final int max = 100;
    int size = 0;

    do {
    UpdateRequest request = new UpdateRequest(followedId, yesterday, offset, max);
    Map<String, String> posts = feedClient.getPosts(request);
    size = posts.size();

    posts.forEach((key,value) -> {
      FeedEntity toAdd = new FeedEntity();
      toAdd.setUserId(followerId);
      toAdd.setPostId(key);
      toAdd.setPosterId(value);

      entities.add(toAdd);
      });

    } while (size == max);

    feedRepository.saveAll(entities);
  }

  @Transactional
  public void connectionDeleted(String followerId, String followedId){
    feedRepository.saveAll(
        feedRepository.findByUserIdAndPosterId(followerId, followedId)
        .stream()
        .peek(entity -> entity.setStatus(FeedStatus.DELETED))
        .toList()
        );
  }

  @Transactional
  public void postConfirmed(String postId, String posterId){
    final int max = 100;
    int offset = 0;
    int size = 0;

    Set<FeedEntity> entities = new HashSet<>();

    do{
      FollowersRequest request = new FollowersRequest(posterId, offset, max);
      List<String> followersId = feedClient.getFollowers(request);
      size = followersId.size();

    followersId.forEach(
      followerId -> {
        FeedEntity entity = new FeedEntity();
        entity.setPostId(postId);
        entity.setUserId(followerId);
        entity.setPosterId(posterId);
        entities.add(entity);
      });
    } while (size == max);

    feedRepository.saveAll(entities);
  }

  @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
  @Transactional
  public void removeOld(){
    LocalDate now = LocalDate.now();
    Date yesterday = Date.from(now.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    feedRepository.deleteAllByTimestampBefore(yesterday);
  }

  @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
  @Transactional
  public void removeSeenOrDeleted(){
    feedRepository.deleteAllByStatus(FeedStatus.DELETED);
    feedRepository.deleteAllByStatus(FeedStatus.SEEN);
  }
}
