package com.picshare.post_service.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.picshare.post_service.client.PostClient;
import com.picshare.post_service.dto.PostRequest;
import com.picshare.post_service.dto.PostResponse;
import com.picshare.post_service.dto.UpdateDto;
import com.picshare.post_service.entity.PostEntity;
import com.picshare.post_service.entity.UpdateEntity.UpdateStatus;
import com.picshare.post_service.mapper.PostMapper;
import com.picshare.post_service.mapper.UpdateMapper;
import com.picshare.post_service.repository.PostRepository;
import com.picshare.post_service.repository.UpdateRepository;
import com.picshare.post_service.service.exceptions.ClientErrorException;
import com.picshare.post_service.service.exceptions.ExternalException;
import com.picshare.post_service.service.exceptions.PostNotFoundException;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class PostService {

  private final UpdateRepository updateRepository;
  private final UpdateMapper updateMapper;
  private final PostRepository postRepository;
  private final PostClient client;
  private final PostMapper postMapper;


  public void store(MultipartFile image, String jsonData, String userId) throws ExternalException, ClientErrorException, IOException{
    ObjectMapper objectMapper = new ObjectMapper();
    PostRequest data = objectMapper.readValue(jsonData, PostRequest.class);

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
