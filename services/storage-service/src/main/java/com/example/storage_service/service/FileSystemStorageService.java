package com.example.storage_service.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.example.storage_service.client.StorageClient;
import com.example.storage_service.service.exceptions.StorageException;
import com.example.storage_service.service.exceptions.StorageFileNotFoundException;
import com.example.storage_service.service.util.WebpManager;

@Service
public class FileSystemStorageService implements StorageService{

  private static final String MEDIA = "media";
  private static final String PREVIEW = "preview";
  private final StorageClient client;
  private final Path mediaRoot;

  @Autowired
  public FileSystemStorageService(StorageClient client, StorageProperties properties){
    this.client = client;
    this.mediaRoot = Paths.get(properties.getMediaroot());
  }

  @Override
  public void store(InputStream data, String filename) {
      Path destination = createDirectory(filename);
      Path[] paths = getPaths(destination);
      try(OutputStream outputStreamMedia = Files.newOutputStream(paths[0], StandardOpenOption.CREATE);
          OutputStream outputStreamThumbnail = Files.newOutputStream(paths[1], StandardOpenOption.CREATE)){

        WebpManager.manage(data, outputStreamMedia, outputStreamThumbnail);

          } catch (IOException e) {
            throw new StorageException("Error while writing file", e);
          }

  }

  private Path[] getPaths(Path destination) {
      Path[] paths = new Path[2];
      paths[0] = destination.resolve(MEDIA);
      paths[1] = destination.resolve(PREVIEW);
      return paths;
  }

  private Path createDirectory(String filename){
    Path destination = this.mediaRoot.resolve(Paths.get(filename)).normalize().toAbsolutePath();
    try {
      Files.createDirectories(destination);
    } catch (IOException e) {
      throw new StorageException("File cannot be saved");
    }
    return destination;
  }

  @Override
  public Resource load(String filename) {
    Path toRead = Path.of(mediaRoot.toString(), filename, MEDIA);
    try {
      return new UrlResource(toRead.toUri());
    } catch (Exception e) {
      throw new StorageFileNotFoundException("Impossible to read selected image");
    }
  }

  @Override
  public Stream<Resource> loadThumbnail(List<String> filenames) {
    return filenames.stream()
      .map(name -> Path.of(mediaRoot.toString(), name, PREVIEW))
      .map(path -> {
        try {
          return new UrlResource(path.toUri());
        } catch (Exception e) {
            throw new StorageFileNotFoundException("Impossible to read selected image");
        }
      });
  }

  @Override
  public void deleteAll() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
  }

}
