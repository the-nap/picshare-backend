package com.picshare.storage_service.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.picshare.storage_service.event.StorageEventProducer;
import com.picshare.storage_service.service.exceptions.NoMediaException;
import com.picshare.storage_service.service.exceptions.StorageException;
import com.picshare.storage_service.service.exceptions.UploadException;
import com.picshare.storage_service.service.util.WebpManager;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MinioService implements StorageService{

  private final MinioClient minioClient;
  private final StorageEventProducer eventProducer;
  private final String bucketName;

  private static final int MEDIA = 0;
  private static final int PREVIEW = 1;

  public MinioService(StorageEventProducer producer, MinioClient minioClient, 
      @Value("${minio.bucket.name}") String bucketName){

      this.eventProducer = producer;
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
  public void store(MultipartFile file, String id) {
    List<Path> source;
    try {
      source = prepareMedia(file);
    } catch (IOException e) {
      this.eventProducer.sendPostSaveFailureEvent(id);
      return;
    }

    try{
      storeInBucket(
          getMediaPath(id), source.get(MEDIA));
    } catch(Exception e){
      this.eventProducer.sendPostSaveFailureEvent(id);
      delete(getMediaPath(id));
      throw new UploadException(String.format("Error while uploading media: %s", id));
    }

    try {
      storeInBucket(
          getPreviewPath(id), source.get(PREVIEW));
    } catch(Exception e){
      this.eventProducer.sendPostSaveFailureEvent(id);
      delete(getMediaPath(id));
      throw new UploadException(String.format("Error while uploading preview: %s", id));
    }

    this.eventProducer.sendPostSaveSuccessEvent(id);
  }
  
  @Override
  public void storeAvatar(MultipartFile input, String id){
    Path source = null;
    try {
      source = prepareAvatar(input);
    } catch (IOException e) {
      try {
        Files.deleteIfExists(source);
      } catch (IOException e1) {
        throw new StorageException(String.format("Failed to delete temporary file"));
      }
      throw new UploadException(String.format("Error while uploading avatar for: %s", id));
    }

    try{
      storeInBucket(
          getAvatarPath(id), source);
    } catch(Exception e){
      delete(getAvatarPath(id));
      throw new UploadException(String.format("Error while uploading avatar: %s", id));
    }
  }

  @Override
  public void deleteMedia(String id){
    delete(String.format("media/%s/", id));
  }

  @Override
  public void deleteAvatar(String id){
    delete(String.format("avatar/%s", id));
  }

  private void delete(String toDelete){
    try{
      minioClient.removeObject(
          RemoveObjectArgs.builder()
          .object(toDelete)
          .bucket(bucketName)
          .build());
    } catch(Exception e){
      throw new StorageException("Error while deleting at: " + toDelete);
    }
  }

  @Override
  public InputStreamResource serveMedia(String id) {
    return search(getMediaPath(id));
  }

  @Override
  public InputStreamResource servePreview(String id) {
    return search(getPreviewPath(id));
  }

  @Override
  public InputStreamResource serveAvatar(String id) {
    return search(getAvatarPath(id));
  }

  @Override
  public String toString() {
    return "MinioService []";
  }

  private InputStreamResource search(String toLook){
    try{ 
      InputStream stream = minioClient.getObject(
          GetObjectArgs.builder()
          .object(toLook)
          .bucket(bucketName)
          .build());
      return new InputStreamResource(stream);
    }catch(ErrorResponseException e){
      throw new NoMediaException("Media not found, defaulting...");
    }
    catch(Exception e){
      throw new StorageException("Storage exception: " + e);
    }
  }

  private String getAvatarPath(String id) {
    return String.format("avatar/%s.webp", id);
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

  private List<Path> prepareMedia(MultipartFile file) throws IOException {
    List<Path> temps = new LinkedList<>();

    temps.add(Files.createTempFile("media-", ".webp"));
    temps.add(Files.createTempFile("preview-", ".webp"));

    try(OutputStream outMedia = Files.newOutputStream(temps.get(MEDIA));
        OutputStream outPreview = Files.newOutputStream(temps.get(PREVIEW));
        InputStream input = file.getResource().getInputStream()){

      WebpManager.toWebp(input, outMedia, outPreview);

    }
    return temps;
  }

  private Path prepareAvatar(MultipartFile file) throws IOException {
    Path temp;

    temp = Files.createTempFile("avatar-", ".webp");

    try(OutputStream out = Files.newOutputStream(temp);
        InputStream stream = file.getInputStream()){

      WebpManager.toWebp(stream, out, null);
      
    }
    return temp;
  }

  private void storeInBucket(String filename, Path temp) throws Exception {
    try(InputStream input = Files.newInputStream(temp)) {
      minioClient.putObject(
          PutObjectArgs.builder()
          .bucket(bucketName)
          .object(filename)
          .stream(input, Files.size(temp), -1)
          .contentType("image/webp")
          .build());
    } finally {
      try {
        Files.deleteIfExists(temp);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
