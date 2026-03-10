package com.picshare;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.keycloak.component.ComponentModel;
import org.keycloak.http.simple.SimpleHttp;
import org.keycloak.http.simple.SimpleHttpRequest;
import org.keycloak.http.simple.SimpleHttpResponse;
import org.keycloak.models.KeycloakSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiClient {

  private final SimpleHttp simpleHttp;
  private final String baseUrl;
  private final ObjectMapper objectMapper;

  public ApiClient(KeycloakSession session, ComponentModel model){
    this.simpleHttp = SimpleHttp.create(session);
    this.baseUrl = model.get(PicshareUserStorageProviderFactory.USER_API_BASE_URL);
    this.objectMapper = new ObjectMapper();
  }

  public PicshareUser searchUsers(String key, String toSearch){
    String url = String.format("%s/users/auth/", baseUrl);
    return searchUsersRequest(url, key, toSearch);
  }

  private PicshareUser searchUsersRequest(String url, String key, String toSearch) {
    SimpleHttpRequest request = prepareGetRequest(url);
    if(key != null){
      request.param("key", key);
    }
    if (toSearch != null){
      request.param("value", toSearch);
    }
    return handleRequest(request);
  }

  private PicshareUser handleRequest(SimpleHttpRequest request){
    AtomicReference<PicshareUser> result = new AtomicReference<>();
    try (SimpleHttpResponse response = request.asResponse()) {
      if (response.getStatus() >= 300) {
        log.error("Error response from server: {}, error: {}, url: {}", response.getStatus(), response.asString(), request.getUrl());
      } else {
        result.set(objectMapper.readValue(response.asString(), PicshareUser.class));
      }
    } catch (IOException e){
      log.error("Exception during calling url %s: %s".formatted(request.getUrl(), e.getMessage()));
    }
    return result.get();
  }

  private SimpleHttpRequest prepareGetRequest(String url) {
    return simpleHttp.doGet(url).acceptJson();
  }

}
