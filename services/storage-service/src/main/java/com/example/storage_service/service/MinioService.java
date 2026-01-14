package com.example.storage_service.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.example.storage_service.service.util.WebpManager;

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
import lombok.AllArgsConstructor;

@Service
public class MinioService implements StorageService{

  private final MinioClient minioClient;
  private final String bucketName;

  public MinioService(MinioClient minioClient, 
      @Value("${minio.bucket.name}") String bucketName){
      this.minioClient = minioClient;
      this.bucketName = bucketName;

      try {
        this.minioClient.makeBucket(MakeBucketArgs.builder()
            .bucket(bucketName)
            .build()
            );
      } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
          | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
          | IllegalArgumentException | IOException e) {
        throw new RuntimeException("Error in creating bucket: " + e.getMessage());
          }
  }

  @Override
  public void store(InputStream file, String id) {
    Path[] temp = decodeToFile(file);

    String basePath = getBasePath(id);
    String mediaPath = getMediaPath(basePath);
    String thumbPath = getThumbPath(basePath);

    storeMedia(mediaPath, temp[0]);
    storeThumbnail(thumbPath, temp[1]);
  }

  private String getBasePath(String id) {
    return "images/" + id + "/";
  }

  private String getMediaPath(String id) {
    return getBasePath(id) + "media";
  }

  private String getThumbPath(String id) {
    return getBasePath(id) + "thumb";
  }

  private void storeMedia(String filename, Path temp) {
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

  private void storeThumbnail(String filename, Path temp) {
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

  private Path[] decodeToFile(InputStream file) {
    Path[] temp = new Path[2];
    try {
      temp[0] = Files.createTempFile("image-", ".webp");
      temp[1] = Files.createTempFile("thumb-", ".webp");
    } catch(IOException e){
      throw new RuntimeException("Cannot create file: " + e.getMessage());
    }
    try(OutputStream out0 = Files.newOutputStream(temp[0], StandardOpenOption.CREATE_NEW); OutputStream out1 = Files.newOutputStream(temp[1], StandardOpenOption.CREATE_NEW)){
      OutputStream[] outputs = new OutputStream[]{ out0, out1 };
      WebpManager.toWebp(file,outputs);
    } catch(IOException e){}
    return temp;
  }

  @Override
  public String toString() {
    return "MinioService []";
  }

  @Override
  public Resource serveMedia(String id) {

    try (InputStream stream = minioClient.getObject(GetObjectArgs.builder().object(getMediaPath(id)).build())) {
      InputStreamResource resource = new InputStreamResource(stream);

      } catch(Exception e){
      }
    return null;
  }

  @Override
  public Resource serveThumbnail(String id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'loadThumbnail'");
  }

  @Override
  public void deleteAll() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
  }


}
