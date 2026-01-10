package com.example.storage_service.service.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.imgscalr.Scalr;

import com.example.storage_service.service.exceptions.StorageException;

public class WebpManager {

  public static void manage(InputStream in, OutputStream outMedia, OutputStream outThumbnail) throws IOException{
    BufferedImage tempImage = ImageIO.read(in);
    if(tempImage == null)
      throw new StorageException("Image not available");
    saveToDisk(tempImage, outMedia, 0.85f);
    tempImage.flush();
    BufferedImage thumbnail = resize(tempImage, 300);
    saveToDisk(thumbnail, outThumbnail, 0.45f);
    thumbnail.flush();
  }


  private static BufferedImage resize(BufferedImage base, int width){
    return Scalr.resize(base, Scalr.Method.BALANCED, Scalr.Mode.AUTOMATIC, width);
  }

  private static void saveToDisk(BufferedImage image, OutputStream out, float quality) throws IOException{
    ImageWriter writer = getWriter();
    ImageWriteParam param = setQuality(writer, quality);

    writeToDisk(out, image, writer, param);
  }

  private static void writeToDisk(OutputStream out, BufferedImage image, ImageWriter writer, ImageWriteParam param)
      throws IOException {
      try(ImageOutputStream ios = ImageIO.createImageOutputStream(out)) {
        writer.setOutput(ios);
        writer.write(null, new IIOImage(image, null, null), param);
      } finally {
        writer.dispose();
      }
  }

  private static ImageWriteParam setQuality(ImageWriter writer, float quality) {
    ImageWriteParam ret = writer.getDefaultWriteParam();
    if(!ret.canWriteCompressed())
      throw new StorageException("Writer not valid");
    ret.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    ret.setCompressionQuality(quality);
    return ret;
  }

  private static ImageWriter getWriter() {
    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("webp");
    if(!writers.hasNext())
      throw new StorageException("Writer not found");
    return writers.next();
  }


}
