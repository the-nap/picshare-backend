package com.picshare.post_service.service.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.picshare.post_service.client.PostClient;
import com.picshare.post_service.event.PostEventProducer;
import com.picshare.post_service.service.dto.PostRequest;
import com.picshare.post_service.service.dto.PostResponse;
import com.picshare.post_service.service.dto.UpdateRequest;
import com.picshare.post_service.service.entity.PostEntity;
import com.picshare.post_service.service.entity.PostStatus;
import com.picshare.post_service.service.entity.TagEntity;
import com.picshare.post_service.service.exceptions.ClientErrorException;
import com.picshare.post_service.service.exceptions.ExternalException;
import com.picshare.post_service.service.exceptions.PostNotFoundException;
import com.picshare.post_service.service.mapper.PostMapper;
import com.picshare.post_service.service.repository.PostRepository;
import com.picshare.post_service.service.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final TagRepository tagRepository;
  private final PostClient client;
  private final PostMapper postMapper;
  private final PostEventProducer eventProducer;

  @Transactional
  public void store(MultipartFile image, PostRequest data, String userId) throws ExternalException, ClientErrorException, IOException{

    PostEntity entity = postMapper.toEntity(data);

    Set<TagEntity> persistentTags = entity.getTags()
      .stream()
      .map(tag -> {
        
        TagEntity finalTag = tagRepository.findByTagName(tag.getTagName())

          .orElseGet(() -> {
            return new TagEntity(tag.getTagName());
          });
        finalTag.addPost(entity);
        return tagRepository.save(finalTag);
      })
      .collect(Collectors.toSet());

    entity.setTags(persistentTags);
    entity.setUserId(userId);
    entity.setStatus(PostStatus.PENDING);
    postRepository.save(entity);
    try{
      client.upload(image, entity.getId());
    } catch(ExternalException e){
    }
  }
  
  @Transactional(readOnly = true)
  public Map<String, String> getPosts(UpdateRequest request){
    return  postRepository.findByUserIdAndCreationDateAfter(request.getUserId(), request.getDate())
      .stream()
      .collect(Collectors.toMap(
          entity -> entity.getId(),
          entity -> entity.getUserId())
      );
  }

  @Transactional
  public Integer addLike(String userId, String postId){
    PostEntity entity = this.postRepository.findById(postId)
      .orElseThrow(() -> new PostNotFoundException(String.format("Post not found with id: %s", postId)));
    entity.addLike(userId);
    postRepository.save(entity);
    return entity.getLikedBy().size();
  }

  public boolean likes(String userId, String postId){
    PostEntity entity = this.postRepository.findById(postId)
      .orElseThrow(() -> new PostNotFoundException(String.format("Post not found with id: %s", postId)));
    return entity.getLikedBy().contains(userId);
  }

  @Transactional
  public void confirm(String id){
    PostEntity entity = this.postRepository.findByIdAndStatus(id, PostStatus.PENDING)
      .orElseThrow(() -> new PostNotFoundException(String.format("Post not found with id: %s", id)));
    entity.setStatus(PostStatus.CONFIRMED);
    postRepository.save(entity);
    this.eventProducer.sendPostConfirmedEvent(entity.getUserId(), entity.getId());
  }

  @Transactional
  public void deleteByUser(String userId){
    Streamable<PostEntity> toDelete;
    int offset = 0;
    do {
      toDelete = this.postRepository.findByUserId(userId, PageRequest.of(offset, 999));
      toDelete
        .stream()
        .forEach(entity -> deletePost(entity.getId()));

      offset++;

      postRepository.saveAll(toDelete);

    } while (!toDelete.isEmpty());
  }

  @Transactional
  public void deletePost(String id){
    PostEntity entity = postRepository.findById(id)
      .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
    entity.setStatus(PostStatus.DELETED);

    postRepository.save(entity);

    this.eventProducer.sendPostDeletedEvent(id);
  }

  public PostResponse serve(String id){
    return this.postMapper.toDto(
      this.postRepository.findById(id)
      .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id)));
  }

  public List<PostResponse> getPosts(List<String> ids){
    return ids.stream()
      .map(id -> this.serve(id))
      .collect(Collectors.toList());
  }

  public List<PostResponse> getPostsByUser(String id, int offset, int max){
    return this.postRepository.findByUserId(id, PageRequest.of(offset, max, Sort.by("creationDate").descending()))
      .map((entity) -> {

        PostResponse result = this.postMapper.toDto(entity);
        return result;

      })
      .toList();
  }

  public List<PostResponse> getPostByTag(String tag, int offset, int max){
    return this.postRepository.findByTag(tag, PageRequest.of(
        offset, max, 
        Sort.by("creationDate").descending()))
      .map(entity -> postMapper.toDto(entity))
      .toList();
  }

}
