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
import org.springframework.stereotype.Component;

import com.luciad.imageio.webp.WebPWriteParam;

@Component
public class WebpManager {

  private static final int MAX_DIMENSION = 576;

  public static void toWebp(InputStream originalImage, OutputStream originalOutput, OutputStream previewOutput) throws IOException{
    BufferedImage original = ImageIO.read(originalImage);

    BufferedImage preview;
    if(original.getHeight() > MAX_DIMENSION && original.getWidth() > MAX_DIMENSION){
      preview = Scalr.resize(original, Scalr.Method.SPEED, MAX_DIMENSION);
    }else{
      preview = new BufferedImage(original.getColorModel(), original.copyData(null), original.isAlphaPremultiplied(), null);
    }

    ImageWriter writer = ImageIO.getImageWritersByFormatName("webp").next();
    
    if(originalOutput != null)
      writeOriginal(originalOutput, original, writer);

    if(previewOutput != null)
      writePreview(previewOutput, preview, writer);
  }

  private static void writePreview(OutputStream output, BufferedImage preview, ImageWriter writer)
      throws IOException {
    try(ImageOutputStream ios = ImageIO.createImageOutputStream(output)) {
      writer.setOutput(ios);

      //set compression
      WebPWriteParam param = new WebPWriteParam(writer.getLocale());
      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      param.setCompressionType(param.getCompressionTypes()[WebPWriteParam.LOSSLESS_COMPRESSION]);
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
      WebPWriteParam param = new WebPWriteParam(writer.getLocale());
      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      param.setCompressionType(param.getCompressionTypes()[WebPWriteParam.LOSSLESS_COMPRESSION]);
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
