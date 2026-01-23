package com.picshare.post_service.service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


  public void store(InputStream data, PostRequest metadata) throws ExternalException, ClientErrorException{
    PostEntity entity = requestMapper.toEntity(metadata);
    postRepository.save(entity);
    String url;
    try {
      url = client.upload(data, entity.getId());
      entity.setImageUrl(url);
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

  public PostResponse serve(Long id) throws PostNotFoundException{
    Optional<PostEntity> post = postRepository.findById(id);
    if(post.isEmpty())
      throw new PostNotFoundException("Post not found with id: " + id);
    return responseMapper.toDto(post.get());
  }

  public List<UpdateDto> serveUpdates(){
    
    return updateRepository.findTop100ByUpdateStatusOrderByIdUserId(UpdateStatus.PENDING)
      .stream()
      .map(entity -> updateMapper.toDto(entity))
      .collect(Collectors.toList());
  }
}
