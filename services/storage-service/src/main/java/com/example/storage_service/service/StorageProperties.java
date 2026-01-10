package com.example.storage_service.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties("storage")
public class StorageProperties {

  private String mediaroot;
}
