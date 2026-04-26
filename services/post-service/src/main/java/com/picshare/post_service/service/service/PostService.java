package com.picshare.post_service.service.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.picshare.post_service.client.PostClient;
import com.picshare.post_service.service.dto.PostRequest;
import com.picshare.post_service.service.dto.PostResponse;
import com.picshare.post_service.service.dto.UpdateDto;
import com.picshare.post_service.service.entity.PostEntity;
import com.picshare.post_service.service.entity.UpdateEntity.UpdateStatus;
import com.picshare.post_service.service.exceptions.ClientErrorException;
import com.picshare.post_service.service.exceptions.ExternalException;
import com.picshare.post_service.service.exceptions.PostNotFoundException;
import com.picshare.post_service.service.mapper.PostMapper;
import com.picshare.post_service.service.mapper.UpdateMapper;
import com.picshare.post_service.service.repository.PostRepository;
import com.picshare.post_service.service.repository.UpdateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

  private final UpdateRepository updateRepository;
  private final UpdateMapper updateMapper;
  private final PostRepository postRepository;
  private final PostClient client;
  private final PostMapper postMapper;


  public void store(MultipartFile image, PostRequest data, String userId) throws ExternalException, ClientErrorException, IOException{

    PostEntity entity = postMapper.toEntity(data);
    entity.setUserId(userId);
    postRepository.save(entity);
    try {
      client.upload(image, entity.getId());
      postRepository.save(entity);
    } catch (RuntimeException | IOException e) {
      throw e;
    }
  }

  public PostResponse serve(String id) throws PostNotFoundException{
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

  public List<UpdateDto> serveUpdates(){
    
    return updateRepository.findTop100ByStatusOrderByIdUserId(UpdateStatus.PENDING)
      .stream()
      .map(entity -> updateMapper.toDto(entity))
      .collect(Collectors.toList());
  }
}
