package com.picshare.post_service.service;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.picshare.post_service.client.PostClient;
import com.picshare.post_service.dto.PostRequest;
import com.picshare.post_service.dto.PostResponse;
import com.picshare.post_service.entity.PostEntity;
import com.picshare.post_service.entity.PostEntity.PostStatus;
import com.picshare.post_service.mapper.PostRequestMapper;
import com.picshare.post_service.mapper.PostResponseMapper;
import com.picshare.post_service.repository.PostRepository;
import com.picshare.post_service.service.exceptions.ClientErrorException;
import com.picshare.post_service.service.exceptions.ExternalException;
import com.picshare.post_service.service.exceptions.PostNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository repository;
  private final PostClient client;
  private final PostRequestMapper requestMapper;
  private final PostResponseMapper responseMapper;


  public void store(InputStream data, PostRequest metadata) throws ExternalException, ClientErrorException{
    PostEntity entity = requestMapper.toEntity(metadata);
    repository.save(entity);
    String url;
    try {
      url = client.upload(data, entity.getId());
      entity.setImageUrl(url);
      entity.setStatus(PostStatus.PUBLISHED);
      repository.save(entity);
    } catch (ExternalException | ClientErrorException e) {
      compensate(entity);
      throw e;
    }
  }

  private void compensate(PostEntity entity) {
    entity.setStatus(PostStatus.FAILED);
  }

  public PostResponse serve(Long id) throws PostNotFoundException{
    Optional<PostEntity> post = repository.findById(id);
    if(post.isEmpty())
      throw new PostNotFoundException("Post not found with id: " + id);
    return responseMapper.toDto(post.get());
  }

}
