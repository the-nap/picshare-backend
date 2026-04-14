package com.picshare.post_service.service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.picshare.post_service.client.PostClient;
import com.picshare.post_service.dto.PostRequest;
import com.picshare.post_service.dto.PostResponse;
import com.picshare.post_service.dto.UpdateDto;
import com.picshare.post_service.entity.PostEntity;
import com.picshare.post_service.entity.PostEntity.PostStatus;
import com.picshare.post_service.entity.UpdateEntity.UpdateStatus;
import com.picshare.post_service.mapper.PostRequestMapper;
import com.picshare.post_service.mapper.PostResponseMapper;
import com.picshare.post_service.mapper.UpdateMapper;
import com.picshare.post_service.repository.PostRepository;
import com.picshare.post_service.repository.UpdateRepository;
import com.picshare.post_service.service.exceptions.ClientErrorException;
import com.picshare.post_service.service.exceptions.ExternalException;
import com.picshare.post_service.service.exceptions.PostNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

  private final UpdateRepository updateRepository;
  private final UpdateMapper updateMapper;
  private final PostRepository postRepository;
  private final PostClient client;
  private final PostRequestMapper requestMapper;
  private final PostResponseMapper responseMapper;


  public void store(InputStream data, PostRequest metadata, String userId) throws ExternalException, ClientErrorException{
    PostEntity entity = requestMapper.toEntity(metadata);
    entity.setUserId(userId);
    postRepository.save(entity);
    try {
      client.upload(data, entity.getId());
      entity.setStatus(PostStatus.PUBLISHED);
      postRepository.save(entity);
    } catch (ExternalException | ClientErrorException e) {
      compensate(entity);
      throw e;
    }
  }

  private void compensate(PostEntity entity) {
    entity.setStatus(PostStatus.FAILED);
  }

  public PostResponse serve(String id) throws PostNotFoundException{
    return this.responseMapper.toDto(
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

        PostResponse result = this.responseMapper.toDto(entity);
        result.setUrl(result.getUrl().concat("/preview.webp"));
        return result;

      })
      .toList();
  }

  public List<PostResponse> getPostByTag(String tag, int offset, int max){
    return this.postRepository.findByTag(tag, PageRequest.of(
        offset, max, 
        Sort.by("creationDate").descending()))
      .map(entity -> responseMapper.toDto(entity))
      .toList();
  }

  public List<UpdateDto> serveUpdates(){
    
    return updateRepository.findTop100ByStatusOrderByIdUserId(UpdateStatus.PENDING)
      .stream()
      .map(entity -> updateMapper.toDto(entity))
      .collect(Collectors.toList());
  }
}
