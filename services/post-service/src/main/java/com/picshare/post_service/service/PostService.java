package com.picshare.post_service.service;

import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.picshare.post_service.client.PostClient;
import com.picshare.post_service.dto.ImageMetadata;
import com.picshare.post_service.entity.PostEntity;
import com.picshare.post_service.mapper.PostMapper;
import com.picshare.post_service.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository repository;
  private final PostClient client;
  private final PostMapper mapper;


  public void store(InputStream data, ImageMetadata metadata) {
    PostEntity entity = mapper.toEntity(metadata);
    repository.save(entity);
    if(client.upload(data, entity.getId().toString())){
      entity.setMediaPending(false);
      repository.save(entity);
    }
  }
}
