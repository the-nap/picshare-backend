package com.example.storage_service.service.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.imgscalr.Scalr;

public class WebpManager {

  public static void toWebp(InputStream originalImage, OutputStream[] outputs) throws IOException{
    BufferedImage original = ImageIO.read(originalImage);
    BufferedImage thumbnail = Scalr.resize(original, Scalr.Method.SPEED, 300);

    ImageWriter writer = ImageIO.getImageWritersByFormatName("webp").next();
    
    try(ImageOutputStream ios = ImageIO.createImageOutputStream(outputs[0])) {
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

    try(ImageOutputStream ios = ImageIO.createImageOutputStream(outputs[1])) {
      writer.setOutput(ios);

      //set compression
      ImageWriteParam param = writer.getDefaultWriteParam();
      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      param.setCompressionQuality(0.45f);

      //write media to stream
      writer.write(null, new IIOImage(thumbnail, null, null), param);
    } finally {
      writer.dispose();
    }
  }
  
  public static BufferedImage read(InputStream input) throws IOException{
    BufferedImage image = ImageIO.read(input);
    if(image == null)
      throw new IOException("Unsupported Image");
    return image;
  }
}
