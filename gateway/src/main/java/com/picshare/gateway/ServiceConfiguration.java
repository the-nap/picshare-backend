package com.picshare.gateway;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "gateway")
public class ServiceConfiguration {

  private Map<String,ServiceDetails> services;

  public ServiceDetails getService(String name){
    return services.get(name);
  }

  @Data
  public static class ServiceDetails {
    private String uri;
    private String fallback;
    private String path;
  }
}
