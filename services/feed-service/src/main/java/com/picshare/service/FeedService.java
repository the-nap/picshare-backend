package com.picshare.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.picshare.DTO.FeedDto;
import com.picshare.mapper.FeedMapper;
import com.picshare.repository.FeedRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FeedService {

  private final FeedRepository feedRepository;
  private final FeedMapper feedMapper;

  public List<Long> getFeed(Long id){
    return feedRepository.findByUserIdOrderByTimestampDesc(id)
      .stream()
      .map(entity -> entity.getPostId())
      .collect(Collectors.toList());
  }
  
  public void markAsSeen(Long userId, Long postId){
    feedRepository.delete(feedRepository.findByUserIdAndPostId(userId, postId).getFirst());
  }

  public void add(Long userId, Long postId){
    FeedDto feed = new FeedDto(userId, postId);
    feedRepository.save(feedMapper.toEntity(feed));
  }

}
