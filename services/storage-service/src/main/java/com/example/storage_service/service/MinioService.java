package com.example.storage_service.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import com.example.storage_service.service.exceptions.StorageException;
import com.example.storage_service.service.util.WebpManager;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.annotation.PostConstruct;

@Service
public class MinioService implements StorageService{

  private final MinioClient minioClient;
  private final String bucketName;

  private static final int MEDIA = 0;
  private static final int PREVIEW = 1;

  public MinioService(MinioClient minioClient, 
      @Value("${minio.bucket.name}") String bucketName){
      this.minioClient = minioClient;
      this.bucketName = bucketName;
  }

  @PostConstruct
  private void initializeBucket(){
    try {
      boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
      if (!found){
        this.minioClient.makeBucket(MakeBucketArgs.builder()
            .bucket(bucketName)
            .build()
            );
      }
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
        | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
        | IllegalArgumentException | IOException e) {
      throw new StorageException("Error in creating bucket: " + e.getMessage() + "exceptionType: " + e.getCause());
        }
  }

  @Override
  public void store(InputStream file, String id) {
    List<Path> source = prepareMedia(file);

    storeInBucket(
        getMediaPath(id), source.get(MEDIA));

    storeInBucket(
        getPreviewPath(id), source.get(PREVIEW));
  }
  
  @Override
  public void storeAvatar(InputStream input, String id){
    Path source = prepareAvatar(input);

    storeInBucket(
        getAvatarPath(id), source);

  }

  @Override
  public InputStreamResource serveAvatar(String id) {
    try (InputStream stream = minioClient.getObject(
          GetObjectArgs.builder()
          .object(getAvatarPath(id))
          .build())){
      return new InputStreamResource(stream);
          } catch(Exception e){
            throw new StorageException("Storage error:" + e);
          }
  }


  @Override
  public InputStreamResource serveMedia(String id) {
    try (InputStream stream = minioClient.getObject(
          GetObjectArgs.builder()
          .object(getMediaPath(id))
          .build())) {
      return new InputStreamResource(stream);
    } catch(Exception e){
      throw new StorageException("Storage error: " + e);
    }
  }

  @Override
  public InputStreamResource servePreview(String id) {
    try(InputStream stream = minioClient.getObject(
          GetObjectArgs.builder()
          .bucket(bucketName)
          .object(getPreviewPath(id))
          .build())) {
      return new InputStreamResource(stream);
      } catch(Exception e) {
        throw new StorageException("Storage error: " + e);
      }
  }

  @Override
  public String toString() {
    return "MinioService []";
  }

  private String getAvatarPath(String id) {
    return String.format("avatar/%s", id);
  }

  private String getMediaPath(String id){
    return String.format("%s/%s/media.webp",getBasePath(), id);
  }
  
  private String getPreviewPath(String id){
    return String.format("%s/%s/preview.webp",getBasePath(), id);
  }

  private String getBasePath(){
    return "images";
  }

  private List<Path> prepareMedia(InputStream file) {
    List<Path> temps = new LinkedList<>();
    try {
      temps.add(Files.createTempFile("media-", ".webp"));
      temps.add(Files.createTempFile("preview-", ".webp"));
    } catch(IOException e){
      throw new RuntimeException("Cannot create file: " + e.getMessage());
    }
    try(OutputStream outMedia = Files.newOutputStream(temps.get(MEDIA), StandardOpenOption.CREATE_NEW);
        OutputStream outPreview = Files.newOutputStream(temps.get(PREVIEW), StandardOpenOption.CREATE_NEW)){
      WebpManager.toWebp(file, outMedia, outPreview);
    } catch(IOException e){}
    return temps;
  }

  private Path prepareAvatar(InputStream file) {
    Path temp;
    try {
      temp = Files.createTempFile("avatar-", ".webp");
    } catch(IOException e){
      throw new RuntimeException("Cannot create file: " + e.getMessage());
    }
    try(OutputStream out = Files.newOutputStream(temp, StandardOpenOption.CREATE_NEW)){
      WebpManager.toWebp(file, out);
    } catch(IOException e) {}
    return temp;
  }

  private void storeInBucket(String filename, Path temp) {
    try(InputStream input = Files.newInputStream(temp)) {
      minioClient.putObject(
          PutObjectArgs.builder()
          .bucket(bucketName)
          .object(filename)
          .stream(input, Files.size(temp), -1)
          .contentType("image/webp")
          .build());
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      try {
        Files.deleteIfExists(temp);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
