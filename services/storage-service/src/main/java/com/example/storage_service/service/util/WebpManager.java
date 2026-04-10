package com.example.storage_service.service.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.imgscalr.Scalr;

public class WebpManager {

  public static void toWebp(InputStream originalImage, OutputStream originalOutput, OutputStream previewOutput) throws IOException{
    BufferedImage original = ImageIO.read(originalImage);
    BufferedImage preview = Scalr.resize(original, Scalr.Method.SPEED, 300);

    ImageWriter writer = ImageIO.getImageWritersByFormatName("webp").next();
    
    writeOriginal(originalOutput, original, writer);

    writePreview(previewOutput, preview, writer);
  }

  public static void toWebp(InputStream inputStream, OutputStream out) throws IOException{
    BufferedImage media = ImageIO.read(inputStream);
    
    ImageWriter writer = ImageIO.getImageWritersByFormatName("webp").next();
    
    writeOriginal(out, media, writer);
  }

  private static void writePreview(OutputStream output, BufferedImage preview, ImageWriter writer)
      throws IOException {
    try(ImageOutputStream ios = ImageIO.createImageOutputStream(output)) {
      writer.setOutput(ios);

      //set compression
      ImageWriteParam param = writer.getDefaultWriteParam();
      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      param.setCompressionQuality(0.45f);

      //write media to stream
      writer.write(null, new IIOImage(preview, null, null), param);
    } finally {
      writer.dispose();
    }
  }

  private static void writeOriginal(OutputStream output, BufferedImage original, ImageWriter writer) throws IOException {
    try(ImageOutputStream ios = ImageIO.createImageOutputStream(output)) {
      writer.setOutput(ios);

      //set compression
      ImageWriteParam param = writer.getDefaultWriteParam();
      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      param.setCompressionQuality(0.85f);

      //write media to stream
      writer.write(null, new IIOImage(original, null, null), param);
    } finally {
      writer.reset();
    }
  }
  
  public static BufferedImage read(InputStream input) throws IOException{
    BufferedImage image = ImageIO.read(input);
    if(image == null)
      throw new IOException("Unsupported Image");
    return image;
  }
}
